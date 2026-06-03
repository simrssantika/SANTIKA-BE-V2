package com.santika.simrs.shared.file.temp

import java.util.UUID

data class TempFileDto(
    val id: UUID,
    val token: String,
    val storedName: String
)
