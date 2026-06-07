package com.santika.simrs.global.jwt

import com.santika.simrs.global.exception.UnauthorizedException
import com.santika.simrs.shared.user.UserResponseDto
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(jwtConfig: JwtConfig) {
    private val secretKey: SecretKey = Keys.hmacShaKeyFor(jwtConfig.secretKey.toByteArray())
    private val validityInMs = jwtConfig.expiration.toLong() * 1000
    private val refreshValidityInMs = jwtConfig.refreshExpiration.toLong() * 1000

    /** [jti] mengikat token ke baris user_sessions (lihat single-session enforcement). */
    fun generateToken(username: String?, profile: UserResponseDto, jti: String): String {
        val now = Date()
        return Jwts.builder()
            .id(jti)
            .subject(username)
            .claims().add("profile", profile).add("iat", now)
            .expiration(Date(now.time + validityInMs)).and()
            .signWith(secretKey)
            .compact()
    }

    fun generateRefreshToken(username: String?, jti: String): String {
        val now = Date()
        return Jwts.builder()
            .id(jti)
            .subject(username)
            .issuedAt(now)
            .expiration(Date(now.time + refreshValidityInMs))
            .signWith(secretKey)
            .compact()
    }

    fun validateToken(token: String?): Boolean {
        return try {
            val claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
            !claims.payload.expiration.before(Date())
        } catch (e: Exception) {
            throw UnauthorizedException()
        }
    }

    fun getUsername(token: String?): String =
        Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).payload.subject

    fun getJti(token: String?): String =
        Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).payload.id
}
