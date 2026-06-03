package com.santika.simrs.module.master.dto.request

import com.santika.simrs.module.master.infrastructure.entities.WilayahProvinsiEntity

data class WilayahProvinsiReq(
    val kodeProvinsi: String,
    val namaProvinsi: String
) {
    companion object {
        fun toProvinsiEntity(req: WilayahProvinsiReq) = WilayahProvinsiEntity().apply {
            kodeProvinsi = req.kodeProvinsi
            namaProvinsi = req.namaProvinsi

        }

        fun toUpdateProvinsiEntity(req: WilayahProvinsiReq, entity: WilayahProvinsiEntity) = entity.apply {
            kodeProvinsi = req.kodeProvinsi
            namaProvinsi = req.namaProvinsi
        }
    }
}
