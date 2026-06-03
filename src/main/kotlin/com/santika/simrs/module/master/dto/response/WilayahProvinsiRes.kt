package com.santika.simrs.module.master.dto.response

import com.santika.simrs.module.master.infrastructure.entities.WilayahProvinsiEntity
import java.util.*


data class WilayahProvinsiRes(
    val id: UUID,
    val kodeProvinsi: String,
    val namaProvinsi: String
) {
    companion object {
        fun ofProvinsi(entity: WilayahProvinsiEntity) = WilayahProvinsiRes(
            id = entity.id!!,
            kodeProvinsi = entity.kodeProvinsi!!,
            namaProvinsi = entity.namaProvinsi!!
        )
    }
}
