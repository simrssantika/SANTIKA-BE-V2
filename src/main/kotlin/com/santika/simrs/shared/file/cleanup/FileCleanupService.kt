package com.santika.simrs.shared.file.cleanup

import com.santika.simrs.shared.file.lock.FileUploadLockService
import com.santika.simrs.shared.file.temp.TempFileEntity
import com.santika.simrs.shared.file.temp.TempFileRepo
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime

@Service
class FileCleanupService(
    private val tempFileRepo: TempFileRepo,
    private val lockService: FileUploadLockService,
    @Value($$"${app.cleanup.batch-size:100}") private val batchSize: Int
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Scheduled(cron = $$"${app.cleanup.cron:0 0 * * * *}")
    fun cleanupExpiredTempFiles() {
        if (lockService.hasActiveUploads()) {
            log.info("Cleanup ditunda — ada upload aktif")
            return
        }

        if (!lockService.tryAcquireCleanupLock()) {
            log.debug("Cleanup lock dipakai instance lain, skip")
            return
        }

        try {
            var totalDeleted = 0
            var batch: List<TempFileEntity>

            do {
                batch = tempFileRepo.findAllExpired(LocalDateTime.now()).take(batchSize)

                if (lockService.hasActiveUploads()) {
                    log.info("Upload masuk di tengah cleanup, berhenti (sudah hapus $totalDeleted)")
                    return
                }

                deleteBatch(batch)
                totalDeleted += batch.size

            } while (batch.size == batchSize)

            if (totalDeleted > 0)
                log.info("Cleanup selesai — $totalDeleted temp file dihapus")

        } finally {
            lockService.releaseCleanupLock()
        }
    }

    @Transactional
    fun deleteBatch(files: List<TempFileEntity>) {
        files.forEach { temp ->
            runCatching { Files.deleteIfExists(Paths.get(temp.path)) }
                .onFailure { log.warn("Gagal hapus file fisik: ${temp.path} — ${it.message}") }
        }
        tempFileRepo.deleteAll(files)
    }
}
