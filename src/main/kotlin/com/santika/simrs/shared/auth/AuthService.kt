package com.santika.simrs.shared.auth

import com.santika.simrs.global.cache.CacheProvider
import com.santika.simrs.global.exception.DataNotFoundException
import com.santika.simrs.global.exception.UnauthorizedException
import com.santika.simrs.global.jwt.JwtConfig
import com.santika.simrs.global.jwt.JwtTokenProvider
import com.santika.simrs.global.jwt.SessionRevocationStore
import com.santika.simrs.shared.user.UserRepository
import com.santika.simrs.shared.user.UserResponseDto
import jakarta.persistence.EntityManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val cacheProvider: CacheProvider,
    private val passwordEncoder: PasswordEncoder,
    private val sessionRepository: UserSessionRepository,
    private val revocationStore: SessionRevocationStore,
    private val entityManager: EntityManager,
    jwtConfig: JwtConfig
) {
    private val refreshTtlSeconds = jwtConfig.refreshExpiration.toLong()

    /**
     * Single-session: validasi kredensial → advisory lock per-user → revoke semua sesi
     * aktif lama (DB + DragonFly) → buat sesi baru + jti → terbitkan token.
     */
    @Transactional
    fun login(request: LoginRequestDto, ip: String?, userAgent: String?): LoginResponseDto {
        val user = userRepository.findByUsername(request.username)
            .orElseThrow { DataNotFoundException("User tidak ditemukan") }

        if (!passwordEncoder.matches(request.password, user.password))
            throw IllegalArgumentException("Username atau password salah")

        val userId = user.id!!
        // Cegah race dua login bersamaan; lock dilepas otomatis saat commit/rollback.
        entityManager.createNativeQuery("SELECT pg_advisory_xact_lock(:key)")
            .setParameter("key", userId.mostSignificantBits)
            .singleResult

        val now = LocalDateTime.now()
        val activeSessions = sessionRepository.findByUserIdAndRevokedAtIsNull(userId)
        activeSessions.forEach {
            it.revokedAt = now
            it.revokedReason = "NEW_LOGIN"
        }
        sessionRepository.saveAll(activeSessions)
        activeSessions.forEach { revocationStore.revoke(it.jti.toString()) }

        val jti = UUID.randomUUID()
        sessionRepository.save(UserSessionEntity().apply {
            this.userId = userId
            this.jti = jti
            deviceInfo = userAgent?.take(255)
            ipAddress = ip?.take(64)
            this.userAgent = userAgent?.take(512)
            createdAt = now
            expiresAt = now.plusSeconds(refreshTtlSeconds)
        })

        val profile = UserResponseDto.of(user)
        val token = jwtTokenProvider.generateToken(user.username, profile, jti.toString())
        val refreshToken = jwtTokenProvider.generateRefreshToken(user.username, jti.toString())

        cacheProvider.clearUserAuthoritiesCache(user.username)
        cacheProvider.clearUserPrincipalCache(user.username)

        return LoginResponseDto(token = token, refreshToken = refreshToken, profile = UserPrincipalDto.of(user))
    }

    /** Rotasi jti: terbitkan jti baru pada baris sesi & matikan jti lama (deteksi reuse). */
    @Transactional
    fun refresh(refreshToken: String?): LoginResponseDto {
        if (!jwtTokenProvider.validateToken(refreshToken))
            throw UnauthorizedException("Refresh token tidak valid")

        val oldJti = jwtTokenProvider.getJti(refreshToken)
        val username = jwtTokenProvider.getUsername(refreshToken)

        val session = sessionRepository.findByJti(UUID.fromString(oldJti))
            ?: throw UnauthorizedException("Sesi tidak ditemukan atau sudah dirotasi")
        if (session.revokedAt != null)
            throw UnauthorizedException("Sesi sudah tidak aktif")

        val user = userRepository.findByUsername(username)
            .orElseThrow { UnauthorizedException("User tidak ditemukan") }

        val newJti = UUID.randomUUID()
        session.jti = newJti
        session.lastUsedAt = LocalDateTime.now()
        sessionRepository.save(session)
        revocationStore.revoke(oldJti) // access token lama langsung mati

        val token = jwtTokenProvider.generateToken(user.username, UserResponseDto.of(user), newJti.toString())
        val newRefreshToken = jwtTokenProvider.generateRefreshToken(user.username, newJti.toString())

        return LoginResponseDto(token = token, refreshToken = newRefreshToken, profile = UserPrincipalDto.of(user))
    }

    /** Logout device ini: revoke sesi milik jti pada access token (best-effort) + DragonFly. */
    @Transactional
    fun logout(accessToken: String?) {
        if (accessToken.isNullOrBlank()) return
        val jti = try {
            jwtTokenProvider.getJti(accessToken)
        } catch (e: Exception) {
            return // token tak terbaca/kedaluwarsa → cukup andalkan clear cookie di controller
        }

        sessionRepository.findByJti(UUID.fromString(jti))?.let { session ->
            if (session.revokedAt == null) {
                session.revokedAt = LocalDateTime.now()
                session.revokedReason = "LOGOUT"
                sessionRepository.save(session)
            }
        }
        revocationStore.revoke(jti)
    }

    @Cacheable(value = ["userAuthorities"], key = "#username")
    @Transactional(readOnly = true)
    fun getRolePermissionUser(username: String): List<String> {
        val user = userRepository.findByUsername(username)
            .orElseThrow { UnauthorizedException("User tidak ditemukan") }
        val permissions = user.roles.flatMap { it.permissions }.map { it.name }
        val roles = user.roles.map { it.name }
        return (permissions + roles).distinct()
    }

    @Cacheable("userPrincipalDetail", key = "#username")
    @Transactional(readOnly = true)
    fun userPrincipalDetail(username: String): UserPrincipalDto {
        val user = userRepository.findByUsername(username)
            .orElseThrow { DataNotFoundException("User tidak ditemukan") }
        return UserPrincipalDto.of(user)
    }
}
