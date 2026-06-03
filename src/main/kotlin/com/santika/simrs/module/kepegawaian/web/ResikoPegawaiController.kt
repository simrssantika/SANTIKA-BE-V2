package com.santika.simrs.module.kepegawaian.web

import com.santika.simrs.global.response.BaseResponse
import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.global.response.PageResponse
import com.santika.simrs.global.response.Response.isSuccess
import com.santika.simrs.global.response.SliceResponse
import com.santika.simrs.global.response.toSliceResponse
import com.santika.simrs.module.kepegawaian.dto.request.ResikoPegawaiReq
import com.santika.simrs.module.kepegawaian.dto.response.ResikoPegawaiRes
import com.santika.simrs.module.kepegawaian.service.ResikoPegawaiService
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/kepegawaian/resiko")
class ResikoPegawaiController(private val service: ResikoPegawaiService) {

    @GetMapping
    fun findAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?
    ): BaseResponse<PageResponse<ResikoPegawaiRes>> {
        val result = service.findAll(PageRequest.of(page, size), search)
        return PageResponse.of(result.map { ResikoPegawaiRes.of(it) }).isSuccess("Resiko retrieved successfully")
    }

    @GetMapping("/options")
    fun options(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) search: String?
    ): BaseResponse<SliceResponse<OptionsRes>> =
        service.options(PageRequest.of(page, size), search).toSliceResponse().isSuccess("OK")

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): BaseResponse<ResikoPegawaiRes> =
        ResikoPegawaiRes.of(service.findById(id)).isSuccess("Resiko retrieved successfully")

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody request: ResikoPegawaiReq): BaseResponse<Nothing?> {
        service.save(request)
        return null.isSuccess("Resiko created successfully", 201)
    }

    @PatchMapping("/{id}")
    fun update(@PathVariable id: UUID, @RequestBody request: ResikoPegawaiReq): BaseResponse<Nothing?> {
        service.update(request, id)
        return null.isSuccess("Resiko updated successfully")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): BaseResponse<Nothing?> {
        service.delete(id)
        return null.isSuccess("Resiko deleted successfully")
    }
}
