package com.santika.simrs.global.jwt

import com.santika.simrs.global.response.BaseResponse
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
    private val authService: AuthService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val authHeader = request.getHeader("Authorization")
            authHeader?.let {
                if (it.startsWith("Bearer ")) {
                    val token = it.substring(7)
                    if (jwtTokenProvider.validateToken(token)) {
                        val username = jwtTokenProvider.getUsername(token)
                        val authorities = authService.getRolePermissionUser(username)
                            .map { auth -> SimpleGrantedAuthority(auth) }
                        val principal = authService.userPrincipalDetail(username)
                        val auth = UsernamePasswordAuthenticationToken(principal, null, authorities)
                        auth.details = WebAuthenticationDetailsSource().buildDetails(request)
                        SecurityContextHolder.getContext().authentication = auth
                    }
                }
            }
            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json"
            val body = BaseResponse("error", HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", null)
            response.writer?.write(jacksonObjectMapper().writeValueAsString(body))
        }
    }
}
