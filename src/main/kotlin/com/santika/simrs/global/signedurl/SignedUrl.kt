package com.santika.simrs.global.signedurl

data class SignedUrl(
    val url: String,        // path + query (expires & signature) — siap dipakai langsung
    val expiresAt: Long,    // epoch second kapan URL kadaluarsa
    val signature: String
)
