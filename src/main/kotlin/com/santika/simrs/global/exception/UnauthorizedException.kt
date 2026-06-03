package com.santika.simrs.global.exception

class UnauthorizedException(override val message: String = "Unauthorized") : RuntimeException(message)
