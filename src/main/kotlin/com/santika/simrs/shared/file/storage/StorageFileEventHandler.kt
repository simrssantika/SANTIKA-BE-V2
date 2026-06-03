package com.santika.simrs.shared.file.storage

import com.santika.simrs.shared.file.lock.FileUploadLockService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import java.nio.file.Files
import java.nio.file.Paths

@Component
class StorageFileEventHandler(
    private val lockService: FileUploadLockService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    // TX commit → hapus file fisik dari disk
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onDeleted(event: StorageFileEvent.Deleted) {
        runCatching { Files.deleteIfExists(Paths.get(event.path)) }
            .onSuccess { log.info("File storage dihapus: ${event.path}") }
            .onFailure { log.warn("Gagal hapus file storage: ${event.path} — ${it.message}") }
    }

    // TX commit → daftarkan token baru ke Redis
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onRolledBack(event: StorageFileEvent.RolledBack) {
        lockService.registerUpload(event.token)
    }
}
