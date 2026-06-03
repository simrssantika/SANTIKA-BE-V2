package com.santika.simrs.global.jwt

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JwtConfig {

    @Value("\${jwt.secret}")
    lateinit var secretKey: String

    @Value("\${jwt.expiration}")
    lateinit var expiration: String

    @Value("\${jwt.refreshExpiration}")
    lateinit var refreshExpiration: String
}
