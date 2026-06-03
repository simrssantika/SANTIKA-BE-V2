package com.santika.simrs.module.kepegawaian.dto.response

import java.util.UUID

data class DokterListRes(
    val id: UUID?,
    val namaDokter: String?,
    val nip: String?,
    val telephoneDokter: String?,
    val isActive: Boolean?,
    val spesialis: SpesialisRef?
) {
    data class SpesialisRef(val id: UUID?, val namaSpesialis: String?)
}
