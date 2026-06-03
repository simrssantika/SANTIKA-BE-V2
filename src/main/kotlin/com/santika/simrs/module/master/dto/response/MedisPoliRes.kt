package com.santika.simrs.module.master.dto.response

import com.santika.simrs.module.master.infrastructure.entities.MedisPoliEntity
import java.util.UUID

data class MedisPoliRes(
    val id: UUID?,
    val kodePoli: String?,
    val namaPoli: String?
) {
    companion object {
        fun ofPoli(entity: MedisPoliEntity) = MedisPoliRes(
            id = entity.id,
            kodePoli = entity.kodePoli,
            namaPoli = entity.namaPoli
        )
    }
}
