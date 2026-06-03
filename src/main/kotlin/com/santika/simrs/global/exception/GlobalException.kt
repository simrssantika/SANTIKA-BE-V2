package com.santika.simrs.global.exception

import com.santika.simrs.global.response.BaseResponse
import com.santika.simrs.global.response.Response.isError
import com.santika.simrs.shared.file.common.FileException
import org.apache.coyote.BadRequestException
import org.springframework.context.NoSuchMessageException
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class GlobalException(private val env: Environment) {

    private val isProd get() = env.activeProfiles.contains("prod")

    private fun safeMessage(e: Throwable, fallback: String = "Internal Server Error") =
        if (isProd) fallback else e.message

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElement(e: NoSuchElementException): BaseResponse<*> =
        null.isError(HttpStatus.NOT_FOUND.value(), e.message)

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFound(e: NoHandlerFoundException): BaseResponse<*> =
        null.isError(HttpStatus.NOT_FOUND.value(), e.message)

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFound(e: NoResourceFoundException): BaseResponse<*> =
        null.isError(HttpStatus.NOT_FOUND.value(), e.message)

    @ExceptionHandler(DataNotFoundException::class)
    fun handleDataNotFound(e: DataNotFoundException): BaseResponse<*> =
        null.isError(HttpStatus.NOT_FOUND.value(), e.message)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(e: IllegalArgumentException): BaseResponse<*> =
        null.isError(HttpStatus.BAD_REQUEST.value(), e.message)

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequest(e: BadRequestException): BaseResponse<*> =
        null.isError(HttpStatus.BAD_REQUEST.value(), e.message)

    @ExceptionHandler(FieldValidationException::class)
    fun handleFieldValidation(e: FieldValidationException): BaseResponse<*> =
        e.errors.isError(HttpStatus.BAD_REQUEST.value(), "Validation Failed")

    @ExceptionHandler(NoSuchMessageException::class)
    fun handleNoSuchMessage(e: NoSuchMessageException): BaseResponse<*> =
        null.isError(HttpStatus.BAD_REQUEST.value(), e.message)

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(e: UnauthorizedException): BaseResponse<*> =
        null.isError(HttpStatus.UNAUTHORIZED.value(), e.message)

    @ExceptionHandler(AuthorizationDeniedException::class)
    fun handleAccessDenied(e: AuthorizationDeniedException): BaseResponse<*> =
        null.isError(HttpStatus.FORBIDDEN.value(), e.message)

    @ExceptionHandler(FileException::class)
    fun handleFileException(e: FileException): BaseResponse<*> = when (e) {
        is FileException.FileInvalidPath -> null.isError(HttpStatus.BAD_REQUEST.value(), e.message)
        else -> null.isError(HttpStatus.NOT_FOUND.value(), e.message)
    }

    @ExceptionHandler(Throwable::class)
    fun handleThrowable(e: Throwable): BaseResponse<*> =
        null.isError(HttpStatus.INTERNAL_SERVER_ERROR.value(), safeMessage(e))
}
