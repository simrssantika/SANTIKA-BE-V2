package com.santika.simrs.module.master.dto.response

import com.santika.simrs.module.master.infrastructure.entities.WilayahKecamatanEntity
import java.util.*

data class WilayahKecamatanRes(
    val id: UUID?,
    val kodeKecamatan: String?,
    val namaKecamatan: String?,
    val kabupaten: KabKotaRes
) {
    companion object {
        fun ofKecamatan(entity: WilayahKecamatanEntity) = WilayahKecamatanRes(
            id = entity.id,
            kodeKecamatan = entity.kodeKecamatan,
            namaKecamatan = entity.namaKecamatan,
            kabupaten = KabKotaRes(
                id = entity.kabKotaId,
                namaKabKota = entity.kabKota?.namaKabKota ?: "-"
            )
        )
    }
}


data class KabKotaRes(
    val id: UUID?,
    val namaKabKota: String,
)
