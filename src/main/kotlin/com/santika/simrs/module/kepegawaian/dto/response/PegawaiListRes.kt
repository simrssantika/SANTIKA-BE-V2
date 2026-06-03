package com.santika.simrs.module.kepegawaian.dto.response

import java.util.UUID

data class PegawaiListRes(
    val id: UUID?,
    val nip: String?,
    val nama: String?,
    val kelamin: String?,
    val jabatan: String?,
    val statusPegawai: String?,
    val isActive: Boolean?,
    val departemen: DepartemenRef?,
    val foto: FotoRef?
) {
    data class DepartemenRef(val id: UUID?, val namaDepartemen: String?)
    data class FotoRef(val id: UUID?, val file: String?)
}
