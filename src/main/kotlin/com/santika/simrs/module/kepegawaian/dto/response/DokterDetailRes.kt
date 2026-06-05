package com.santika.simrs.module.kepegawaian.dto.response

import java.util.UUID

data class DokterDetailRes(
    val id: UUID?,
    val namaDokter: String?,
    val nip: String?,
    val telephoneDokter: String?,
    val emailDokter: String?,
    val alumni: String?,
    val noIjinPraktek: String?,
    val isActive: Boolean?,
    val spesialis: SpesialisRef?,
    val pegawai: PegawaiRef?
) {
    data class SpesialisRef(val id: UUID?, val namaSpesialis: String?)
    data class PegawaiRef(val id: UUID?, val nip: String?, val nama: String?)
}
