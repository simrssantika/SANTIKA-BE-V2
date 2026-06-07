package com.santika.simrs.global.utils

import org.springframework.http.ResponseCookie

const val ACCESS_TOKEN_COOKIE = "access_token"
const val REFRESH_TOKEN_COOKIE = "refresh_token"

/** Cookie HttpOnly generik untuk token auth (Secure=false: dev http LAN). */
private fun buildAuthCookie(name: String, value: String?, maxAgeSeconds: Long): String =
    ResponseCookie.from(name, value ?: "")
        .httpOnly(true)
        .secure(false)
        .path("/")
        .sameSite("Lax")
        .maxAge(maxAgeSeconds)
        .build()
        .toString()

fun buildAccessCookie(value: String?, maxAgeSeconds: Long): String =
    buildAuthCookie(ACCESS_TOKEN_COOKIE, value, maxAgeSeconds)

fun buildRefreshCookie(value: String?, maxAgeSeconds: Long): String =
    buildAuthCookie(REFRESH_TOKEN_COOKIE, value, maxAgeSeconds)

/** Cookie kosong ber-maxAge 0 untuk menghapus cookie di sisi browser (logout). */
fun buildClearCookie(name: String): String = buildAuthCookie(name, "", 0)
