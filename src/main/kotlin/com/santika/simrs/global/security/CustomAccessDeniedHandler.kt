package com.santika.simrs.global.security

import com.santika.simrs.global.response.BaseResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.csrf.CsrfException
import org.springframework.stereotype.Component
import tools.jackson.module.kotlin.jacksonObjectMapper

@Component
class CustomAccessDeniedHandler : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        val responseData = mutableMapOf<String, String>()
        response.status = HttpServletResponse.SC_FORBIDDEN
        response.contentType = "application/json"
        responseData["type"] = "access_denied"
        if (accessDeniedException is CsrfException) {
            responseData["type"] = "csrf_invalid"
        }
        val body = BaseResponse("error", HttpServletResponse.SC_FORBIDDEN, "Forbidden", responseData)
        response.writer?.write(jacksonObjectMapper().writeValueAsString(body))
    }
}
