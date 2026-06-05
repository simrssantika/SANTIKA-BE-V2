package com.santika.simrs.module.kepegawaian.dto.response

import java.util.UUID

data class DokumenPegawaiRes(
    val id: UUID?,
    val namaDokumen: String?,
    val isActive: Boolean,
    val file: FileRef?
) {
    data class FileRef(val id: UUID?, val name: String?)
}
