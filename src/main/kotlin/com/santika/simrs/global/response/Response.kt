package com.santika.simrs.global.response

object Response {
    fun <T> T?.isSuccess(message: String, code: Int = 200) = BaseResponse(status = "OK", code, message, this)

    fun <T> T?.isError(code: Int, message: String?) = BaseResponse(status = "FAILED", code, message, this)
}