package com.santika.simrs.module.kepegawaian.dto.response

import com.santika.simrs.module.kepegawaian.infrastructure.entities.BidangPegawaiEntity
import java.util.UUID

data class BidangPegawaiRes(
    val id: UUID?,
    val kodeBidang: String?,
    val namaBidang: String?
) {
    companion object {
        fun of(entity: BidangPegawaiEntity) = BidangPegawaiRes(
            id = entity.id,
            kodeBidang = entity.kodeBidang,
            namaBidang = entity.namaBidang
        )
    }
}
