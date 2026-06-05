package com.santika.simrs.module.kepegawaian.dto.request

import java.util.UUID

data class DokterReq(
    val pegawaiId: UUID?,
    val namaDokter: String,
    val nip: String?,
    val telephoneDokter: String?,
    val emailDokter: String?,
    val spesialisId: UUID?,
    val alumni: String?,
    val noIjinPraktek: String?,
    val isActive: Boolean = true
)
