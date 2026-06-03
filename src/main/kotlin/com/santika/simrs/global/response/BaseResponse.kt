package com.santika.simrs.global.response

data class BaseResponse<T>(
    var status: String,
    var code: Int,
    var message: String?,
    var data: T? = null
)
