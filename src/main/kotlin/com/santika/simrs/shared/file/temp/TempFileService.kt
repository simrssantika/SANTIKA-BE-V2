package com.santika.simrs.shared.file.temp

import com.santika.simrs.global.exception.DataNotFoundException
import com.santika.simrs.shared.file.lock.FileUploadLockService
import com.santika.simrs.shared.file.storage.StorageFileEntity
import com.santika.simrs.shared.file.storage.StorageFileRepo
import com.santika.simrs.shared.file.storage.StorageFileService
import com.santika.simrs.shared.file.support.FileParallel
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

@Service
class TempFileService(
    private val tempFileRepo: TempFileRepo,
    private val storageFileRepo: StorageFileRepo,
    private val lockService: FileUploadLockService,
    private val eventPublisher: ApplicationEventPublisher,
    private val storageFileService: StorageFileService,
    @Value("\${app.temp-dir:/tmp/simrs/temp}") private val tempDir: String,
    @Value("\${app.upload-dir:/tmp/simrs/uploads}") private val uploadDir: String,
    @Value("\${app.temp-file-expiry-hours:24}") private val expiryHours: Long
) {

    // Hasil tulis-file ke disk sebelum disimpan ke DB
    private data class WrittenFile(
        val token: String,
        val originalName: String?,
        val storedName: String,
        val path: String,
        val ext: String?,
        val size: Long,
        val mimeType: String?
    )

    @Transactional
    fun store(files: List<MultipartFile>, uploadedBy: UUID? = null): List<TempFileDto> {
        require(files.isNotEmpty()) { "Tidak ada file yang diupload" }

        // 1. Tulis semua file ke disk secara paralel (virtual threads).
        //    Jika ada yang gagal, file yang terlanjur ditulis dihapus lalu error dilempar.
        val written = writeAllToDisk(files)

        // 2. Simpan record ke DB di thread transaksi (JPA tidak thread-safe).
        val entities = written.map { w ->
            TempFileEntity().also {
                it.originalName = w.originalName
                it.storedName = w.storedName
                it.path = w.path
                it.ext = w.ext
                it.size = w.size
                it.mimeType = w.mimeType
                it.uploadToken = w.token
                it.uploadedBy = uploadedBy
                it.expiresAt = LocalDateTime.now().plusHours(expiryHours)
            }
        }
        val saved = tempFileRepo.saveAll(entities)

        // 3. AFTER_COMMIT   → daftarkan semua token ke Redis
        //    AFTER_ROLLBACK → hapus semua file fisik yang sudah ditulis
        eventPublisher.publishEvent(
            TempFileEvent.Stored(written.map { TempFileEvent.FileRef(it.token, it.path) })
        )

        return saved.map {
            TempFileDto(id = it.id!!, token = it.uploadToken!!, storedName = it.storedName)
        }
    }

    @Transactional
    fun moveToStorage(tempIds: List<UUID>, module: String, entityId: UUID): List<StorageFileEntity> {
        require(tempIds.isNotEmpty()) { "Tidak ada file untuk dipindah" }

        val temps = tempFileRepo.findAllById(tempIds)
        if (temps.size != tempIds.distinct().size)
            throw DataNotFoundException("Sebagian temp file tidak ditemukan")

        val moves = ArrayList<TempFileEvent.MovedToStorage.Move>(temps.size)
        val toSave = temps.map { temp ->
            val destPath = Paths.get(uploadDir, temp.storedName).toString()
            moves.add(
                TempFileEvent.MovedToStorage.Move(
                    token = temp.uploadToken ?: "",
                    sourcePath = temp.path,
                    destPath = destPath
                )
            )
            StorageFileEntity().also {
                it.originalName = temp.originalName
                it.storedName = temp.storedName
                it.path = destPath    // path tujuan final di uploadDir
                it.ext = temp.ext
                it.size = temp.size
                it.mimeType = temp.mimeType
                it.module = module
                it.entityId = entityId
                it.uploadedBy = temp.uploadedBy
                it.movedFromTempId = temp.id
            }
        }

        val saved = storageFileRepo.saveAll(toSave)
        tempFileRepo.deleteAll(temps)

        // AFTER_COMMIT → pindahkan semua file fisik (paralel) + lepas token Redis
        eventPublisher.publishEvent(TempFileEvent.MovedToStorage(moves))

        return saved
    }

    @Transactional
    fun cancelUpload(tokens: List<String>) {
        if (tokens.isEmpty()) return

        val temps = tempFileRepo.findByUploadTokenIn(tokens)
        if (temps.isNotEmpty()) {
            tempFileRepo.deleteAll(temps)
            // AFTER_COMMIT → hapus semua file fisik (paralel) + lepas token Redis
            eventPublisher.publishEvent(
                TempFileEvent.Cancelled(temps.map { TempFileEvent.FileRef(it.uploadToken ?: "", it.path) })
            )
        }

        // Token yang ada di Redis tapi tidak di DB — lepas langsung tanpa event
        val foundTokens = temps.mapNotNull { it.uploadToken }.toSet()
        (tokens.toSet() - foundTokens).forEach { lockService.deregisterUpload(it) }
    }

    fun findById(id: UUID): TempFileEntity =
        tempFileRepo.findById(id).orElseThrow { DataNotFoundException("Temp file tidak ditemukan") }

    @Transactional
    fun <R> updateFile(
        tempIds: List<UUID>,
        oldTempIds: List<UUID?>,
        uploadedBy: UUID? = null,
        updateAction: (List<StorageFileEntity>) -> R
    ): R {
        if (oldTempIds.isNotEmpty()) {
            storageFileService.rollbackToTemp(oldTempIds, uploadedBy)
        }
        val data = moveToStorage(tempIds, "temp", UUID.randomUUID())
        return updateAction(data)

    }

    /**
     * Tulis semua file ke disk secara paralel (virtual threads). Bila ada yang gagal,
     * file yang sudah terlanjur ditulis dihapus (paralel juga) lalu error dilempar ulang
     * supaya transaksi rollback bersih tanpa menyisakan file orphan.
     */
    private fun writeAllToDisk(files: List<MultipartFile>): List<WrittenFile> {
        val writtenPaths = ConcurrentLinkedQueue<String>()
        try {
            return FileParallel.map(files) { file ->
                val ext = file.originalFilename?.substringAfterLast('.', "")
                    ?.let { if (it.isNotBlank()) ".$it" else "" } ?: ""
                val storedName = "${System.currentTimeMillis()}-${UUID.randomUUID()}$ext"
                val dest = Paths.get(tempDir, storedName)

                Files.createDirectories(dest.parent)
                file.transferTo(dest)
                writtenPaths.add(dest.toString())

                WrittenFile(
                    token = UUID.randomUUID().toString(),
                    originalName = file.originalFilename,
                    storedName = storedName,
                    path = dest.toString(),
                    ext = ext.removePrefix("."),
                    size = file.size,
                    mimeType = file.contentType
                )
            }
        } catch (e: Exception) {
            FileParallel.forEach(writtenPaths.toList()) { runCatching { Files.deleteIfExists(Paths.get(it)) } }
            throw e
        }
    }
}
