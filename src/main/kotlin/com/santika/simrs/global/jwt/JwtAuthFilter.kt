package com.santika.simrs.global.jwt

import com.santika.simrs.global.utils.ACCESS_TOKEN_COOKIE
import com.santika.simrs.shared.auth.AuthService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import tools.jackson.module.kotlin.jacksonObjectMapper

@Component
class JwtAuthFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val authService: AuthService,
    private val revocationStore: SessionRevocationStore
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = readAccessToken(request)
        if (token != null) {
            try {
                if (jwtTokenProvider.validateToken(token)) {
                    val jti = jwtTokenProvider.getJti(token)
                    // Sesi sudah di-kick (login device lain / logout / rotasi) → tolak tegas.
                    if (revocationStore.isRevoked(jti)) {
                        writeSessionRevoked(response)
                        return
                    }
                    val username = jwtTokenProvider.getUsername(token)
                    val authorities = authService.getRolePermissionUser(username)
                        .map { SimpleGrantedAuthority(it) }
                    val principal = authService.userPrincipalDetail(username)
                    val auth = UsernamePasswordAuthenticationToken(principal, null, authorities)
                    auth.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = auth
                }
            } catch (e: Exception) {
                // Access token invalid/kedaluwarsa: jangan 401 di sini (cookie ikut terkirim
                // ke endpoint publik & ke /auth/refresh). Biarkan tak terautentikasi —
                // endpoint terlindungi akan 401 lewat CustomAuthEntryPoint.
                SecurityContextHolder.clearContext()
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun readAccessToken(request: HttpServletRequest): String? =
        request.cookies?.firstOrNull { it.name == ACCESS_TOKEN_COOKIE }?.value?.takeIf { it.isNotBlank() }

    private fun writeSessionRevoked(response: HttpServletResponse) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        val body = mapOf(
            "error" to "session_revoked",
            "reason" to "logged_in_from_another_device"
        )
        response.writer?.write(jacksonObjectMapper().writeValueAsString(body))
    }
}
