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
import java.util.UUID

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
    fun rollbackToTemp(storageId: UUID, uploadedBy: UUID? = null): TempFileDto {
        val storage = storageFileRepo.findById(storageId)
            .orElseThrow { DataNotFoundException("Storage file tidak ditemukan") }

        val token = UUID.randomUUID().toString()
        val saved = tempFileRepo.save(TempFileEntity().also {
            it.originalName = storage.originalName
            it.storedName   = storage.storedName
            it.path         = storage.path
            it.ext          = storage.ext
            it.size         = storage.size
            it.mimeType     = storage.mimeType
            it.uploadToken  = token
            it.uploadedBy   = uploadedBy ?: storage.uploadedBy
            it.expiresAt    = LocalDateTime.now().plusHours(expiryHours)
        })

        storage.softDelete()
        storageFileRepo.save(storage)

        // AFTER_COMMIT → registerUpload(token) di Redis
        eventPublisher.publishEvent(StorageFileEvent.RolledBack(token))

        return TempFileDto(id = saved.id!!, token = token, storedName = saved.storedName)
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
