package com.santika.simrs.global.utils

import org.springframework.http.ResponseCookie

fun buildRefreshCookie(value: String?, maxAgeSeconds: Long): String =
    ResponseCookie.from("refresh_token", value)
        .httpOnly(true)
        .secure(false)
        .path("/")
        .sameSite("Lax")
        .maxAge(maxAgeSeconds)
        .build()
        .toString()
