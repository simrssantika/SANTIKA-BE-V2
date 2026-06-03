package com.santika.simrs.module.kepegawaian.dto.response

import com.santika.simrs.module.kepegawaian.infrastructure.entities.DaruratPegawaiEntity
import java.util.UUID

data class DaruratPegawaiRes(
    val id: UUID?,
    val kodeEmergency: String?,
    val namaEmergency: String?
) {
    companion object {
        fun of(entity: DaruratPegawaiEntity) = DaruratPegawaiRes(
            id = entity.id,
            kodeEmergency = entity.kodeEmergency,
            namaEmergency = entity.namaEmergency
        )
    }
}
