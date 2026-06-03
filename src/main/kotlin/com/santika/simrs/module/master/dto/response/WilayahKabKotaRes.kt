package com.santika.simrs.module.master.dto.response

import com.santika.simrs.module.master.infrastructure.entities.WilayahKabKotaEntity
import java.util.*

data class WilayahKabKotaRes(
    val id: UUID,
    val kodeKabKota: String,
    val namaKabKota: String,
    val provinsi: ProvinsiRes
) {
    companion object {
        fun ofKabKota(entity: WilayahKabKotaEntity) = WilayahKabKotaRes(
            id = entity.id!!,
            kodeKabKota = entity.kodeKabKota!!,
            namaKabKota = entity.namaKabKota!!,
            provinsi = ProvinsiRes(
                id = entity.provinsiId,
                namaProvinsi = entity.provinsi?.namaProvinsi!!
            )
        )
    }
}


data class ProvinsiRes(
    val id: UUID?,
    val namaProvinsi: String
)