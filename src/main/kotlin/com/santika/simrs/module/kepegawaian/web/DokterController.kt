package com.santika.simrs.module.kepegawaian.web

import com.santika.simrs.global.response.BaseResponse
import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.global.response.Response.isSuccess
import com.santika.simrs.global.response.SliceResponse
import com.santika.simrs.global.response.toSliceResponse
import com.santika.simrs.module.kepegawaian.dto.request.DokterReq
import com.santika.simrs.module.kepegawaian.dto.response.DokterDetailRes
import com.santika.simrs.module.kepegawaian.dto.response.DokterListRes
import com.santika.simrs.module.kepegawaian.service.DokterService
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/kepegawaian/dokter")
class DokterController(private val service: DokterService) {

    @GetMapping
    fun findAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?,
        @RequestParam(required = false) isActive: Boolean?,
        @RequestParam(required = false) spesialisId: UUID?
    ): BaseResponse<SliceResponse<DokterListRes>> {
        val result = service.findAll(PageRequest.of(page, size), search, isActive, spesialisId)
        return result.toSliceResponse().isSuccess("Dokter retrieved successfully")
    }

    @GetMapping("/options")
    fun options(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) search: String?
    ): BaseResponse<SliceResponse<OptionsRes>> =
        service.options(PageRequest.of(page, size), search).toSliceResponse().isSuccess("OK")

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): BaseResponse<DokterDetailRes> =
        service.findById(id).isSuccess("Dokter retrieved successfully")

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody request: DokterReq): BaseResponse<Nothing?> {
        service.save(request)
        return null.isSuccess("Dokter created successfully", 201)
    }

    @PatchMapping("/{id}")
    fun update(@PathVariable id: UUID, @RequestBody request: DokterReq): BaseResponse<Nothing?> {
        service.update(request, id)
        return null.isSuccess("Dokter updated successfully")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): BaseResponse<Nothing?> {
        service.delete(id)
        return null.isSuccess("Dokter deleted successfully")
    }
}
