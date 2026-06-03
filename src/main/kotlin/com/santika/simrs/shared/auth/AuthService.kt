package com.santika.simrs.shared.auth

import com.santika.simrs.global.cache.CacheProvider
import com.santika.simrs.global.exception.DataNotFoundException
import com.santika.simrs.global.exception.UnauthorizedException
import com.santika.simrs.global.jwt.JwtTokenProvider
import com.santika.simrs.shared.user.UserRepository
import com.santika.simrs.shared.user.UserResponseDto
import org.springframework.cache.annotation.Cacheable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val cacheProvider: CacheProvider,
    private val passwordEncoder: PasswordEncoder
) {

    fun login(request: LoginRequestDto): LoginResponseDto {
        val user = userRepository.findByUsername(request.username)
            .orElseThrow { DataNotFoundException("User tidak ditemukan") }

        if (!passwordEncoder.matches(request.password, user.password))
            throw IllegalArgumentException("Username atau password salah")

        val profile = UserResponseDto.of(user)
        val token = jwtTokenProvider.generateToken(user.username, profile)
        val refreshToken = jwtTokenProvider.generateRefreshToken(user.username)

        cacheProvider.clearUserAuthoritiesCache(user.username)
        cacheProvider.clearUserPrincipalCache(user.username)

        return LoginResponseDto(token = token, refreshToken = refreshToken, profile = UserPrincipalDto.of(user))
    }

    fun refresh(refreshToken: String?): LoginResponseDto {
        if (!jwtTokenProvider.validateToken(refreshToken))
            throw UnauthorizedException("Refresh token tidak valid")

        val username = jwtTokenProvider.getUsername(refreshToken)
        val user = userRepository.findByUsername(username)
            .orElseThrow { UnauthorizedException("User tidak ditemukan") }

        val token = jwtTokenProvider.generateToken(user.username, UserResponseDto.of(user))
        val newRefreshToken = jwtTokenProvider.generateRefreshToken(user.username)

        return LoginResponseDto(token = token, refreshToken = newRefreshToken, profile = UserPrincipalDto.of(user))
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
