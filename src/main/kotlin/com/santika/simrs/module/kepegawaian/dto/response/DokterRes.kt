package com.santika.simrs.module.kepegawaian.dto.response

import com.santika.simrs.module.kepegawaian.infrastructure.entities.DokterEntity
import java.util.UUID

data class DokterRes(
    val id: UUID?,
    val pegawaiId: UUID?,
    val namaDokter: String?,
    val nip: String?,
    val telephoneDokter: String?,
    val emailDokter: String?,
    val spesialisId: UUID?,
    val alumni: String?,
    val noIjinPraktek: String?,
    val isActive: Boolean
) {
    companion object {
        fun of(entity: DokterEntity) = DokterRes(
            id = entity.id,
            pegawaiId = entity.pegawaiId,
            namaDokter = entity.namaDokter,
            nip = entity.nip,
            telephoneDokter = entity.telephoneDokter,
            emailDokter = entity.emailDokter,
            spesialisId = entity.spesialisId,
            alumni = entity.alumni,
            noIjinPraktek = entity.noIjinPraktek,
            isActive = entity.isActive
        )
    }
}
