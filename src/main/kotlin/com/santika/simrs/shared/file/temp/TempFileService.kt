package com.santika.simrs.shared.file.temp

import com.santika.simrs.global.exception.DataNotFoundException
import com.santika.simrs.shared.file.lock.FileUploadLockService
import com.santika.simrs.shared.file.storage.StorageFileEntity
import com.santika.simrs.shared.file.storage.StorageFileRepo
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.util.UUID

@Service
class TempFileService(
    private val tempFileRepo: TempFileRepo,
    private val storageFileRepo: StorageFileRepo,
    private val lockService: FileUploadLockService,
    private val eventPublisher: ApplicationEventPublisher,
    @Value("\${app.temp-dir:/tmp/simrs/temp}") private val tempDir: String,
    @Value("\${app.upload-dir:/tmp/simrs/uploads}") private val uploadDir: String,
    @Value("\${app.temp-file-expiry-hours:24}") private val expiryHours: Long
) {

    @Transactional
    fun store(file: MultipartFile, uploadedBy: UUID? = null): TempFileDto {
        val token = UUID.randomUUID().toString()
        val ext = file.originalFilename?.substringAfterLast('.', "")
            ?.let { if (it.isNotBlank()) ".$it" else "" } ?: ""
        val storedName = "${System.currentTimeMillis()}-${UUID.randomUUID()}$ext"
        val dest = Paths.get(tempDir, storedName)

        // File ditulis ke disk sebelum TX selesai —
        // jika TX rollback, AFTER_ROLLBACK handler akan hapus file ini
        Files.createDirectories(dest.parent)
        file.transferTo(dest)

        val saved = tempFileRepo.save(TempFileEntity().also {
            it.originalName = file.originalFilename
            it.storedName   = storedName
            it.path         = dest.toString()
            it.ext          = ext.removePrefix(".")
            it.size         = file.size
            it.mimeType     = file.contentType
            it.uploadToken  = token
            it.uploadedBy   = uploadedBy
            it.expiresAt    = LocalDateTime.now().plusHours(expiryHours)
        })

        // AFTER_COMMIT   → registerUpload(token) di Redis
        // AFTER_ROLLBACK → deleteIfExists(path) — bersihkan file yang sudah ditulis
        eventPublisher.publishEvent(TempFileEvent.Stored(token, dest.toString()))

        return TempFileDto(id = saved.id!!, token = token, storedName = storedName)
    }

    @Transactional
    fun moveToStorage(tempId: UUID, module: String, entityId: UUID): StorageFileEntity {
        val temp = tempFileRepo.findById(tempId)
            .orElseThrow { DataNotFoundException("Temp file tidak ditemukan") }

        val destPath = Paths.get(uploadDir, temp.storedName).toString()

        val saved = storageFileRepo.save(StorageFileEntity().also {
            it.originalName    = temp.originalName
            it.storedName      = temp.storedName
            it.path            = destPath    // path tujuan final di uploadDir
            it.ext             = temp.ext
            it.size            = temp.size
            it.mimeType        = temp.mimeType
            it.module          = module
            it.entityId        = entityId
            it.uploadedBy      = temp.uploadedBy
            it.movedFromTempId = temp.id
        })

        tempFileRepo.delete(temp)

        // AFTER_COMMIT → Files.move(sourcePath, destPath) + deregisterUpload(token)
        eventPublisher.publishEvent(
            TempFileEvent.MovedToStorage(
                token      = temp.uploadToken ?: "",
                sourcePath = temp.path,
                destPath   = destPath
            )
        )

        return saved
    }

    @Transactional
    fun cancelUpload(token: String) {
        val temp = tempFileRepo.findByUploadToken(token)

        if (temp != null) {
            tempFileRepo.delete(temp)
            // AFTER_COMMIT → deleteIfExists(path) + deregisterUpload(token)
            eventPublisher.publishEvent(TempFileEvent.Cancelled(token, temp.path))
        } else {
            // Token ada di Redis tapi tidak di DB — lepas langsung tanpa event
            lockService.deregisterUpload(token)
        }
    }

    fun findById(id: UUID): TempFileEntity =
        tempFileRepo.findById(id).orElseThrow { DataNotFoundException("Temp file tidak ditemukan") }
}
