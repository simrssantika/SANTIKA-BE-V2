package com.santika.simrs.shared.auth

import com.santika.simrs.global.response.BaseResponse
import com.santika.simrs.global.response.Response.isSuccess
import com.santika.simrs.global.utils.buildRefreshCookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequestDto,
        response: HttpServletResponse
    ): BaseResponse<LoginResponseDto> {
        val result = authService.login(request)
        response.addHeader(
            HttpHeaders.SET_COOKIE,
            buildRefreshCookie(result.refreshToken, 604800)
        )
        return result.isSuccess("Login berhasil")
    }

    @PostMapping("/refresh")
    fun refresh(
        @CookieValue(name = "refresh_token", required = false) refreshToken: String?
    ): BaseResponse<LoginResponseDto> {
        val result = authService.refresh(refreshToken)
        return result.isSuccess("Token diperbarui")
    }
}
