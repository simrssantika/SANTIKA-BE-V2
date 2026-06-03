package com.santika.simrs.module.master.dto.response

import com.santika.simrs.module.master.infrastructure.entities.MedisDiagnosaEntity
import java.util.*

data class MedisDiagnosaRes(
    val id: UUID?,
    val kodeDiagnosa: String?,
    val namaDiagnosa: String?
) {
    companion object {
        fun ofDiagnosa(entity: MedisDiagnosaEntity) = MedisDiagnosaRes(
            id = entity.id,
            kodeDiagnosa = entity.kodeDiagnosa,
            namaDiagnosa = entity.namaDiagnosa
        )
    }
}