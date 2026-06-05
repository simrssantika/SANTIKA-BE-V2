package com.santika.simrs.shared.file.temp

import com.santika.simrs.shared.file.lock.FileUploadLockService
import com.santika.simrs.shared.file.support.FileParallel
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

    // TX commit → daftarkan semua token ke Redis
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onStored(event: TempFileEvent.Stored) {
        event.files.forEach { lockService.registerUpload(it.token) }
    }

    // TX rollback → file sudah ada di disk tapi record gagal disimpan → hapus semua file (paralel)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    fun onStoredRollback(event: TempFileEvent.Stored) {
        FileParallel.forEach(event.files) { ref ->
            runCatching { Files.deleteIfExists(Paths.get(ref.path)) }
                .onSuccess { log.info("File dihapus setelah rollback: ${ref.path}") }
                .onFailure { log.warn("Gagal hapus file setelah rollback: ${ref.path} — ${it.message}") }
        }
    }

    // TX commit → pindahkan semua file fisik temp → storage (paralel) + lepas token Redis
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onMovedToStorage(event: TempFileEvent.MovedToStorage) {
        FileParallel.forEach(event.moves) { move ->
            val dest = Paths.get(move.destPath)
            runCatching {
                Files.createDirectories(dest.parent)
                Files.move(Paths.get(move.sourcePath), dest, StandardCopyOption.REPLACE_EXISTING)
                log.info("File dipindah: ${move.sourcePath} → ${move.destPath}")
            }.onFailure {
                // DB sudah commit, file gagal dipindah — log untuk reconciliation manual
                log.error("SYNC ERROR: DB commit tapi file gagal dipindah: ${move.sourcePath} → ${move.destPath}. Perlu reconciliation.")
            }
        }
        event.moves.forEach { lockService.deregisterUpload(it.token) }
    }

    // TX commit → hapus semua file fisik (paralel) + lepas token Redis
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onCancelled(event: TempFileEvent.Cancelled) {
        FileParallel.forEach(event.files) { ref ->
            runCatching { Files.deleteIfExists(Paths.get(ref.path)) }
                .onFailure { log.warn("Gagal hapus file saat cancel: ${ref.path} — ${it.message}") }
        }
        event.files.forEach { lockService.deregisterUpload(it.token) }
    }
}
