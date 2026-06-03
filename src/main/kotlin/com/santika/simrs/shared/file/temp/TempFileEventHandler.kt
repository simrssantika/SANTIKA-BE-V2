package com.santika.simrs.shared.file.temp

import com.santika.simrs.shared.file.lock.FileUploadLockService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Component
class TempFileEventHandler(
    private val lockService: FileUploadLockService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    // TX commit → daftarkan token ke Redis
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onStored(event: TempFileEvent.Stored) {
        lockService.registerUpload(event.token)
    }

    // TX rollback → file sudah ada di disk tapi record gagal disimpan → hapus file
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    fun onStoredRollback(event: TempFileEvent.Stored) {
        runCatching { Files.deleteIfExists(Paths.get(event.path)) }
            .onSuccess { log.info("File dihapus setelah rollback: ${event.path}") }
            .onFailure { log.warn("Gagal hapus file setelah rollback: ${event.path} — ${it.message}") }
    }

    // TX commit → pindahkan file fisik dari temp ke storage + lepas token Redis
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onMovedToStorage(event: TempFileEvent.MovedToStorage) {
        val dest = Paths.get(event.destPath)
        runCatching {
            Files.createDirectories(dest.parent)
            Files.move(Paths.get(event.sourcePath), dest, StandardCopyOption.REPLACE_EXISTING)
            log.info("File dipindah: ${event.sourcePath} → ${event.destPath}")
        }.onFailure {
            // DB sudah commit, file gagal dipindah — log untuk reconciliation manual
            log.error("SYNC ERROR: DB commit tapi file gagal dipindah: ${event.sourcePath} → ${event.destPath}. Perlu reconciliation.")
        }
        lockService.deregisterUpload(event.token)
    }

    // TX commit → hapus file fisik + lepas token Redis
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onCancelled(event: TempFileEvent.Cancelled) {
        runCatching { Files.deleteIfExists(Paths.get(event.path)) }
            .onFailure { log.warn("Gagal hapus file saat cancel: ${event.path} — ${it.message}") }
        lockService.deregisterUpload(event.token)
    }
}
