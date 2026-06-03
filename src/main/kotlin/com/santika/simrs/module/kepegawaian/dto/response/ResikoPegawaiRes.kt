package com.santika.simrs.module.kepegawaian.dto.response

import com.santika.simrs.module.kepegawaian.infrastructure.entities.ResikoPegawaiEntity
import java.util.UUID

data class ResikoPegawaiRes(
    val id: UUID?,
    val kodeResiko: String?,
    val namaResiko: String?
) {
    companion object {
        fun of(entity: ResikoPegawaiEntity) = ResikoPegawaiRes(
            id = entity.id,
            kodeResiko = entity.kodeResiko,
            namaResiko = entity.namaResiko
        )
    }
}
