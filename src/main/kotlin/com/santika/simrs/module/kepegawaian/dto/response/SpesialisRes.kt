package com.santika.simrs.module.kepegawaian.dto.response

import com.santika.simrs.module.kepegawaian.infrastructure.entities.SpesialisEntity
import java.util.UUID

data class SpesialisRes(
    val id: UUID?,
    val kodeSpesialis: String?,
    val namaSpesialis: String?
) {
    companion object {
        fun of(entity: SpesialisEntity) = SpesialisRes(
            id = entity.id,
            kodeSpesialis = entity.kodeSpesialis,
            namaSpesialis = entity.namaSpesialis
        )
    }
}
