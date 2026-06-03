package com.santika.simrs.module.master.dto.request

import java.util.*

data class WilayahKabKotaReq(
    val provinsiId: UUID,
    val kodeKabKota: String,
    val namaKabKota: String
)
