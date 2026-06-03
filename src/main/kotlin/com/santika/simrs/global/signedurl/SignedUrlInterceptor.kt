package com.santika.simrs.global.signedurl

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

/**
 * Validasi signed URL secara global.
 *
 * Berlaku untuk SEMUA request di bawah path yang didaftarkan (lihat WebConfig).
 * Logika opt-in: request yang TIDAK membawa param `signature` dilewatkan
 * (jatuh ke mekanisme auth normal / JWT). Request yang membawa `signature`
 * WAJIB valid — kalau tidak, ditolak 401.
 *
 * Dengan ini endpoint apa pun bisa diakses via signed URL hanya dengan
 * menyertakan `?expires=...&signature=...`, tanpa perlu JWT.
 */
@Component
class SignedUrlInterceptor(
    private val signedUrlService: SignedUrlService
) : HandlerInterceptor {

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        // Tidak ada signature → bukan signed request, lewatkan ke auth normal
        val signature = request.getParameter("signature") ?: return true

        val expires = request.getParameter("expires")?.toLongOrNull()
        val valid = expires != null &&
            signedUrlService.verifyUrl(request.requestURI, expires, signature)

        if (valid) return true

        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer.write(
            """{"status":401,"message":"Signature tidak valid atau sudah kadaluarsa"}"""
        )
        return false
    }
}
