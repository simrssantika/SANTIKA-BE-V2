package com.santika.simrs.module.kepegawaian.dto.response

import com.santika.simrs.module.kepegawaian.infrastructure.entities.VendorTenagaLuarEntity
import java.util.UUID

data class VendorTenagaLuarRes(
    val id: UUID?,
    val namaVendor: String?,
    val alamatVendor: String?,
    val bidangVendor: String?,
    val telephoneVendor: String?,
    val namaKontakDarurat: String?,
    val kontakDaruratVendor: String?
) {
    companion object {
        fun of(entity: VendorTenagaLuarEntity) = VendorTenagaLuarRes(
            id = entity.id,
            namaVendor = entity.namaVendor,
            alamatVendor = entity.alamatVendor,
            bidangVendor = entity.bidangVendor,
            telephoneVendor = entity.telephoneVendor,
            namaKontakDarurat = entity.namaKontakDarurat,
            kontakDaruratVendor = entity.kontakDaruratVendor
        )
    }
}
