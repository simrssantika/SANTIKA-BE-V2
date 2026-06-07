package com.santika.simrs.shared.auth

import com.santika.simrs.global.jwt.JwtConfig
import com.santika.simrs.global.response.BaseResponse
import com.santika.simrs.global.response.Response.isSuccess
import com.santika.simrs.global.utils.ACCESS_TOKEN_COOKIE
import com.santika.simrs.global.utils.REFRESH_TOKEN_COOKIE
import com.santika.simrs.global.utils.buildAccessCookie
import com.santika.simrs.global.utils.buildClearCookie
import com.santika.simrs.global.utils.buildRefreshCookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService,
    jwtConfig: JwtConfig
) {
    private val accessMaxAge = jwtConfig.expiration.toLong()
    private val refreshMaxAge = jwtConfig.refreshExpiration.toLong()

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequestDto,
        httpRequest: HttpServletRequest,
        response: HttpServletResponse
    ): BaseResponse<UserPrincipalDto> {
        val result = authService.login(
            request,
            ip = httpRequest.remoteAddr,
            userAgent = httpRequest.getHeader(HttpHeaders.USER_AGENT)
        )
        setAuthCookies(response, result)
        return result.profile.isSuccess("Login berhasil")
    }

    @PostMapping("/refresh")
    fun refresh(
        @CookieValue(name = REFRESH_TOKEN_COOKIE, required = false) refreshToken: String?,
        response: HttpServletResponse
    ): BaseResponse<UserPrincipalDto> {
        val result = authService.refresh(refreshToken)
        setAuthCookies(response, result)
        return result.profile.isSuccess("Token diperbarui")
    }

    @PostMapping("/logout")
    fun logout(
        @CookieValue(name = ACCESS_TOKEN_COOKIE, required = false) accessToken: String?,
        response: HttpServletResponse
    ): BaseResponse<Unit> {
        authService.logout(accessToken)
        response.addHeader(HttpHeaders.SET_COOKIE, buildClearCookie(ACCESS_TOKEN_COOKIE))
        response.addHeader(HttpHeaders.SET_COOKIE, buildClearCookie(REFRESH_TOKEN_COOKIE))
        return Unit.isSuccess("Logout berhasil")
    }

    /** Fetch token CSRF: meng-inject [CsrfToken] memicu penulisan cookie XSRF-TOKEN. */
    @GetMapping("/csrf")
    fun csrf(token: CsrfToken): BaseResponse<Map<String, String>> =
        mapOf("header_name" to token.headerName, "token" to token.token)
            .isSuccess("CSRF token")

    private fun setAuthCookies(response: HttpServletResponse, result: LoginResponseDto) {
        response.addHeader(HttpHeaders.SET_COOKIE, buildAccessCookie(result.token, accessMaxAge))
        response.addHeader(HttpHeaders.SET_COOKIE, buildRefreshCookie(result.refreshToken, refreshMaxAge))
    }
}
