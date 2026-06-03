package com.santika.simrs.module.kepegawaian.dto.response

import com.santika.simrs.module.kepegawaian.infrastructure.entities.DokumenPegawaiEntity
import java.util.UUID

data class DokumenPegawaiRes(
    val id: UUID?,
    val pegawaiId: UUID?,
    val namaDokumen: String?,
    val fileId: UUID?,
    val isActive: Boolean
) {
    companion object {
        fun of(entity: DokumenPegawaiEntity) = DokumenPegawaiRes(
            id = entity.id,
            pegawaiId = entity.pegawaiId,
            namaDokumen = entity.namaDokumen,
            fileId = entity.fileId,
            isActive = entity.isActive
        )
    }
}
