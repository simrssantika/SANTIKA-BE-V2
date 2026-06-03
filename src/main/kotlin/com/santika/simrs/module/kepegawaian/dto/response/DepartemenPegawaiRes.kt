package com.santika.simrs.module.kepegawaian.dto.response

import com.santika.simrs.module.kepegawaian.infrastructure.entities.DepartemenPegawaiEntity
import java.util.UUID

data class DepartemenPegawaiRes(
    val id: UUID?,
    val kodeDepartemen: String?,
    val namaDepartemen: String?
) {
    companion object {
        fun of(entity: DepartemenPegawaiEntity) = DepartemenPegawaiRes(
            id = entity.id,
            kodeDepartemen = entity.kodeDepartemen,
            namaDepartemen = entity.namaDepartemen
        )
    }
}
