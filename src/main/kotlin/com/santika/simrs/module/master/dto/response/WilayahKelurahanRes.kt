package com.santika.simrs.module.master.dto.response

import com.santika.simrs.module.master.infrastructure.entities.WilayahKelurahanEntity
import java.util.*

data class WilayahKelurahanRes(
    val id: UUID?,
    val kodeKelurahan: String?,
    val namaKelurahan: String?,
    val kecamatan: KecamatanRes
) {
    companion object {
        fun ofKelurahan(entity: WilayahKelurahanEntity) = WilayahKelurahanRes(
            id = entity.id,
            kodeKelurahan = entity.kodeKelurahan,
            namaKelurahan = entity.namaKelurahan,
            kecamatan = KecamatanRes(
                id = entity.kecamatanId,
                namaKecamatan = null
            )
        )
    }
}

data class KecamatanRes(
    val id: UUID?,
    val namaKecamatan: String?
)