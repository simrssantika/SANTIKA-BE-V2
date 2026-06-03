package com.santika.simrs.shared.file.lock

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant

@Service
class FileUploadLockService(
    private val rateLimitRedisTemplate: StringRedisTemplate
) {
    companion object {
        private const val ACTIVE_UPLOADS_KEY = "file:uploads:active"
        private const val CLEANUP_LOCK_KEY   = "file:cleanup:lock"
        private val UPLOAD_TTL = Duration.ofMinutes(10)
    }

    fun registerUpload(token: String) {
        val expireScore = Instant.now().plus(UPLOAD_TTL).epochSecond.toDouble()
        rateLimitRedisTemplate.opsForZSet().add(ACTIVE_UPLOADS_KEY, token, expireScore)
    }

    fun deregisterUpload(token: String) {
        rateLimitRedisTemplate.opsForZSet().remove(ACTIVE_UPLOADS_KEY, token)
    }

    fun hasActiveUploads(): Boolean {
        val now = Instant.now().epochSecond.toDouble()
        rateLimitRedisTemplate.opsForZSet().removeRangeByScore(ACTIVE_UPLOADS_KEY, 0.0, now)
        return (rateLimitRedisTemplate.opsForZSet().zCard(ACTIVE_UPLOADS_KEY) ?: 0L) > 0
    }

    fun tryAcquireCleanupLock(): Boolean =
        rateLimitRedisTemplate.opsForValue()
            .setIfAbsent(CLEANUP_LOCK_KEY, "1", Duration.ofHours(1)) == true

    fun releaseCleanupLock() {
        rateLimitRedisTemplate.delete(CLEANUP_LOCK_KEY)
    }
}
