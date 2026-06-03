package com.santika.simrs.module.kepegawaian.web

import com.santika.simrs.global.response.BaseResponse
import com.santika.simrs.global.response.PageResponse
import com.santika.simrs.global.response.Response.isSuccess
import com.santika.simrs.module.kepegawaian.dto.request.DokumenPegawaiReq
import com.santika.simrs.module.kepegawaian.dto.response.DokumenPegawaiRes
import com.santika.simrs.module.kepegawaian.service.DokumenPegawaiService
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/kepegawaian/pegawai/{pegawaiId}/dokumen")
class DokumenPegawaiController(private val service: DokumenPegawaiService) {

    @GetMapping
    fun findAll(
        @PathVariable pegawaiId: UUID,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?
    ): BaseResponse<PageResponse<DokumenPegawaiRes>> {
        val result = service.findAllByPegawai(PageRequest.of(page, size), pegawaiId, search)
        return PageResponse.of(result.map { DokumenPegawaiRes.of(it) }).isSuccess("Dokumen retrieved successfully")
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable pegawaiId: UUID, @PathVariable id: UUID): BaseResponse<DokumenPegawaiRes> =
        DokumenPegawaiRes.of(service.findById(id)).isSuccess("Dokumen retrieved successfully")

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(
        @PathVariable pegawaiId: UUID,
        @RequestBody request: DokumenPegawaiReq
    ): BaseResponse<Nothing?> {
        service.save(pegawaiId, request)
        return null.isSuccess("Dokumen created successfully", 201)
    }

    @PatchMapping("/{id}")
    fun update(
        @PathVariable pegawaiId: UUID,
        @PathVariable id: UUID,
        @RequestBody request: DokumenPegawaiReq
    ): BaseResponse<Nothing?> {
        service.update(request, id)
        return null.isSuccess("Dokumen updated successfully")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable pegawaiId: UUID, @PathVariable id: UUID): BaseResponse<Nothing?> {
        service.delete(id)
        return null.isSuccess("Dokumen deleted successfully")
    }
}
