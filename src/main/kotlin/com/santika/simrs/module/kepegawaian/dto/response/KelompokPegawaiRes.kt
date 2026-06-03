package com.santika.simrs.module.kepegawaian.dto.response

import com.santika.simrs.module.kepegawaian.infrastructure.entities.KelompokPegawaiEntity
import java.util.UUID

data class KelompokPegawaiRes(
    val id: UUID?,
    val kodeKelompok: String?,
    val namaKelompok: String?
) {
    companion object {
        fun of(entity: KelompokPegawaiEntity) = KelompokPegawaiRes(
            id = entity.id,
            kodeKelompok = entity.kodeKelompok,
            namaKelompok = entity.namaKelompok
        )
    }
}
