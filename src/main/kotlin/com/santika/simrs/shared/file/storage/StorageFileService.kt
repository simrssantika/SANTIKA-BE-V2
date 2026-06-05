package com.santika.simrs.shared.file.storage

import com.santika.simrs.global.exception.DataNotFoundException
import com.santika.simrs.shared.file.temp.TempFileDto
import com.santika.simrs.shared.file.temp.TempFileEntity
import com.santika.simrs.shared.file.temp.TempFileRepo
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class StorageFileService(
    private val storageFileRepo: StorageFileRepo,
    private val tempFileRepo: TempFileRepo,
    private val eventPublisher: ApplicationEventPublisher,
    @Value("\${app.temp-file-expiry-hours:24}") private val expiryHours: Long
) {

    fun findById(id: UUID): StorageFileEntity =
        storageFileRepo.findById(id).orElseThrow { DataNotFoundException("Storage file tidak ditemukan") }

    fun findByEntity(module: String, entityId: UUID): List<StorageFileEntity> =
        storageFileRepo.findByModuleAndEntityId(module, entityId)

    @Transactional
    fun rollbackToTemp(storageId: List<UUID?>, uploadedBy: UUID? = null): List<TempFileDto> {

        if (storageId.isEmpty()) return emptyList()

        val storage = storageFileRepo.findByIdIn(storageId)

        if (storage.size != storageId.distinct().size) {
            return emptyList()
        }

        val data = storage.map { storage ->
            val token = UUID.randomUUID().toString()
            TempFileEntity().also {
                it.originalName = storage.originalName
                it.storedName = storage.storedName
                it.path = storage.path
                it.ext = storage.ext
                it.size = storage.size
                it.mimeType = storage.mimeType
                it.uploadToken = token
                it.uploadedBy = uploadedBy ?: storage.uploadedBy
                it.expiresAt = LocalDateTime.now().plusHours(expiryHours)
            }

        }


        val saved = tempFileRepo.saveAll(data)

        storage.forEach { it.softDelete() }
        storageFileRepo.saveAll(storage)


        // AFTER_COMMIT → registerUpload(token) di Redis
        data.forEach {
            eventPublisher.publishEvent(StorageFileEvent.RolledBack(it.uploadToken.toString()))
        }

        return saved.map {
            TempFileDto(id = it.id!!, token = it.uploadToken!!, storedName = it.storedName)
        }
    }

    @Transactional
    fun delete(storageId: UUID) {
        val storage = storageFileRepo.findById(storageId)
            .orElseThrow { DataNotFoundException("Storage file tidak ditemukan") }

        // AFTER_COMMIT → deleteIfExists(path)
        eventPublisher.publishEvent(StorageFileEvent.Deleted(storage.path))

        storage.softDelete()
        storageFileRepo.save(storage)
    }
}
