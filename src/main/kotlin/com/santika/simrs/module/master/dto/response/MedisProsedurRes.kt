package com.santika.simrs.module.master.dto.response

import com.santika.simrs.module.master.infrastructure.entities.MedisProsedurEntity
import java.util.*

data class MedisProsedurRes(
    val id: UUID?,
    val kodeProsedur: String?,
    val namaProsedur: String?
) {
    companion object {
        fun ofProsedur(entity: MedisProsedurEntity) = MedisProsedurRes(
            id = entity.id,
            kodeProsedur = entity.kodeProsedur,
            namaProsedur = entity.namaProsedur
        )
    }
}