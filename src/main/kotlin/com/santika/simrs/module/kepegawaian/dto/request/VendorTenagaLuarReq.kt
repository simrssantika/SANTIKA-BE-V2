package com.santika.simrs.module.kepegawaian.dto.request

data class VendorTenagaLuarReq(
    val namaVendor: String,
    val alamatVendor: String?,
    val bidangVendor: String?,
    val telephoneVendor: String?,
    val namaKontakDarurat: String?,
    val kontakDaruratVendor: String?
)
