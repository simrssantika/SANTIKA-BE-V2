package com.santika.simrs.global.signedurl

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets.UTF_8
import java.security.MessageDigest
import java.time.Instant
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Utilitas signing global berbasis HMAC-SHA256.
 *
 * Independen dari user/JWT — validasi murni berdasarkan secret bersama (shared secret).
 * Cocok untuk:
 *  - Signed URL akses file (foto, dokumen) tanpa header Authorization
 *  - Validasi transaksi lintas service (mis. Spring Boot ↔ Go PDF service)
 *  - Tanda tangan payload/callback antar service
 *
 * Dua mode pemakaian:
 *  1. URL  → [signUrl] / [verifyUrl]  — menempelkan expires + signature ke URI
 *  2. Raw  → [sign] / [verify]        — tanda tangan string/payload bebas
 */
@Service
class SignedUrlService(
    @Value("\${app.signed-url.secret}") private val secret: String,
    @Value("\${app.signed-url.default-ttl-seconds:300}") private val defaultTtlSeconds: Long
) {
    companion object {
        private const val ALGO = "HmacSHA256"
    }

    private val encoder = Base64.getUrlEncoder().withoutPadding()

    // ── Raw HMAC — tanda tangan payload bebas (cross-service body, transaction id, dll) ──

    fun sign(payload: String): String {
        val mac = Mac.getInstance(ALGO)
        mac.init(SecretKeySpec(secret.toByteArray(UTF_8), ALGO))
        return encoder.encodeToString(mac.doFinal(payload.toByteArray(UTF_8)))
    }

    fun verify(payload: String, signature: String): Boolean =
        MessageDigest.isEqual(sign(payload).toByteArray(UTF_8), signature.toByteArray(UTF_8))

    // ── Signed URL — berbasis URI dengan masa berlaku ──

    /**
     * Tanda tangani sebuah [path] (URI tanpa query expires/signature).
     * Mengembalikan URL siap pakai: `path?expires=...&signature=...`
     */
    fun signUrl(path: String, ttlSeconds: Long? = null): SignedUrl {
        val expires = Instant.now().epochSecond + (ttlSeconds ?: defaultTtlSeconds)
        val signature = sign(canonical(path, expires))
        val sep = if (path.contains('?')) '&' else '?'
        return SignedUrl(
            url = "$path${sep}expires=$expires&signature=$signature",
            expiresAt = expires,
            signature = signature
        )
    }

    /**
     * Validasi signed URL: cek expiry dulu, lalu cocokkan signature (constant-time).
     * [path] adalah URI tanpa query (mis. `request.requestURI`).
     */
    fun verifyUrl(path: String, expires: Long, signature: String): Boolean {
        if (Instant.now().epochSecond > expires) return false
        return verify(canonical(path, expires), signature)
    }

    // expires ikut ditandatangani → client tidak bisa memperpanjang masa berlaku
    private fun canonical(path: String, expires: Long): String = "$path:$expires"
}
