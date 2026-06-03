package com.santika.simrs.module.kepegawaian.dto.request

import java.util.UUID

data class DokumenPegawaiReq(
    val namaDokumen: String,
    val fileId: UUID?,
    val isActive: Boolean = true
)
