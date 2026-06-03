package com.santika.simrs.module.master.dto.request

import java.util.*

data class WilayahKelurahanReq(
    val kecamatanId: UUID,
    val kodeKelurahan: String,
    val namaKelurahan: String
)
