package com.santika.simrs.module.master.dto.request

data class SupportPenjaminReq(
    val penjamin: String,
    val namaPerusahaanPenjamin: String,
    val alamatPenjamin: String,
    val telephonePenjamin: String,
    val penerimaTagihan: String,
    val isActive: Boolean
)
