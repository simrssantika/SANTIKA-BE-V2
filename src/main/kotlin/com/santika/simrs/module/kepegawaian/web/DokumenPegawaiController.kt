package com.santika.simrs.module.kepegawaian.web

import com.santika.simrs.global.response.BaseResponse
import com.santika.simrs.global.response.Response.isSuccess
import com.santika.simrs.module.kepegawaian.dto.response.DokumenPegawaiRes
import com.santika.simrs.module.kepegawaian.service.DokumenPegawaiService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Read-only. Lifecycle dokumen (create/update/delete) ikut pegawai lewat
 * [com.santika.simrs.module.kepegawaian.service.PegawaiService].
 * Dapat diakses by pegawaiId atau dokterId (di-resolve ke pegawai-nya).
 */
@RestController
@RequestMapping("/api/kepegawaian/dokumen")
class DokumenPegawaiController(private val service: DokumenPegawaiService) {

    @GetMapping
    fun findAll(
        @RequestParam(required = false) pegawaiId: UUID?,
        @RequestParam(required = false) dokterId: UUID?
    ): BaseResponse<List<DokumenPegawaiRes>> =
        service.findAll(pegawaiId, dokterId).isSuccess("Dokumen retrieved successfully")
}
