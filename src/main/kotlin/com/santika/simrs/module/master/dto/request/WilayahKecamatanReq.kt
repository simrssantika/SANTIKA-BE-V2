package com.santika.simrs.module.master.dto.request

import java.util.*

data class WilayahKecamatanReq(
    val kabKotaId: UUID,
    val kodeKecamatan: String,
    val namaKecamatan: String
)
