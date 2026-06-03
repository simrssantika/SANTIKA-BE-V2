package com.santika.simrs.module.master.dto.response

import com.santika.simrs.module.master.infrastructure.entities.SupportPenjaminEntity
import java.util.*

data class SupportPenjaminRes(
    val id: UUID?,
    val penjamin: String?,
    val namaPerusahaanPenjamin: String?,
    val alamatPenjamin: String?,
    val telephonePenjamin: String?,
    val penerimaTagihan: String?,
    val isActive: Boolean
) {
    companion object {
        fun ofPenjamin(entity: SupportPenjaminEntity) = SupportPenjaminRes(
            id = entity.id,
            penjamin = entity.penjamin,
            namaPerusahaanPenjamin = entity.namaPerusahaanPenjamin,
            alamatPenjamin = entity.alamatPenjamin,
            telephonePenjamin = entity.teleponPenjamin,
            penerimaTagihan = entity.penerimaTagihan,
            isActive = entity.isActive
        )
    }
}
