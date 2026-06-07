package com.santika.simrs.global.jwt

import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

/**
 * Daftar-hitam jti di DragonFly. Sebuah jti dianggap VALID kecuali ada key
 * `jwt:session:{jti}` = "revoked". Key ditulis saat: login baru men-kick sesi lama,
 * rotasi jti saat refresh, dan logout. TTL key = sisa masa berlaku access token
 * (setelah itu token sudah kedaluwarsa sendiri, key tak diperlukan lagi).
 *
 * Default (key tidak ada) = valid → request normal TIDAK menyentuh DB sama sekali.
 */
@Component
class SessionRevocationStore(
    private val rateLimitRedisTemplate: StringRedisTemplate,
    jwtConfig: JwtConfig
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val ttl = Duration.ofSeconds(jwtConfig.expiration.toLong())

    fun revoke(jti: String) {
        rateLimitRedisTemplate.opsForValue().set(key(jti), "revoked", ttl)
    }

    /**
     * Cek apakah jti sudah di-revoke. FAIL-OPEN: bila DragonFly tidak bisa dihubungi,
     * anggap belum di-revoke agar autentikasi tetap jalan (access token tetap berumur
     * pendek, jadi dampak outage maksimal hanya menunda kick selama TTL token).
     */
    fun isRevoked(jti: String): Boolean = try {
        rateLimitRedisTemplate.hasKey(key(jti)) == true
    } catch (e: Exception) {
        log.warn("Gagal cek revocation jti di DragonFly, fail-open: {}", e.message)
        false
    }

    private fun key(jti: String) = "jwt:session:$jti"
}
