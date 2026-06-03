package com.santika.simrs.module.kepegawaian.web

import com.santika.simrs.global.response.BaseResponse
import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.global.response.Response.isSuccess
import com.santika.simrs.global.response.SliceResponse
import com.santika.simrs.global.response.toSliceResponse
import com.santika.simrs.module.kepegawaian.dto.request.PegawaiReq
import com.santika.simrs.module.kepegawaian.dto.response.PegawaiDetailRes
import com.santika.simrs.module.kepegawaian.dto.response.PegawaiListRes
import com.santika.simrs.module.kepegawaian.service.PegawaiService
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/kepegawaian/pegawai")
class PegawaiController(private val service: PegawaiService) {

    @GetMapping
    fun findAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?,
        @RequestParam(required = false) isActive: Boolean?,
        @RequestParam(required = false) statusPegawai: String?,
        @RequestParam(required = false) departemenId: UUID?
    ): BaseResponse<SliceResponse<PegawaiListRes>> {
        val result = service.findAll(PageRequest.of(page, size), search, isActive, statusPegawai, departemenId)
        return result.toSliceResponse().isSuccess("Pegawai retrieved successfully")
    }

    @GetMapping("/options")
    fun options(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) search: String?
    ): BaseResponse<SliceResponse<OptionsRes>> =
        service.options(PageRequest.of(page, size), search).toSliceResponse().isSuccess("OK")

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): BaseResponse<PegawaiDetailRes> =
        service.findById(id).isSuccess("Pegawai retrieved successfully")

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody request: PegawaiReq): BaseResponse<Nothing?> {
        service.save(request)
        return null.isSuccess("Pegawai created successfully", 201)
    }

    @PatchMapping("/{id}")
    fun update(@PathVariable id: UUID, @RequestBody request: PegawaiReq): BaseResponse<Nothing?> {
        service.update(request, id)
        return null.isSuccess("Pegawai updated successfully")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): BaseResponse<Nothing?> {
        service.delete(id)
        return null.isSuccess("Pegawai deleted successfully")
    }
}
