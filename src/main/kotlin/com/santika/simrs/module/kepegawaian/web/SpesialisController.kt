package com.santika.simrs.module.kepegawaian.web

import com.santika.simrs.global.response.BaseResponse
import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.global.response.PageResponse
import com.santika.simrs.global.response.Response.isSuccess
import com.santika.simrs.global.response.SliceResponse
import com.santika.simrs.global.response.toSliceResponse
import com.santika.simrs.module.kepegawaian.dto.request.SpesialisReq
import com.santika.simrs.module.kepegawaian.dto.response.SpesialisRes
import com.santika.simrs.module.kepegawaian.service.SpesialisService
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/kepegawaian/spesialis")
class SpesialisController(private val service: SpesialisService) {

    @GetMapping
    fun findAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?
    ): BaseResponse<PageResponse<SpesialisRes>> {
        val result = service.findAll(PageRequest.of(page, size), search)
        return PageResponse.of(result.map { SpesialisRes.of(it) }).isSuccess("Spesialis retrieved successfully")
    }

    @GetMapping("/options")
    fun options(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) search: String?
    ): BaseResponse<SliceResponse<OptionsRes>> =
        service.options(PageRequest.of(page, size), search).toSliceResponse().isSuccess("OK")

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): BaseResponse<SpesialisRes> =
        SpesialisRes.of(service.findById(id)).isSuccess("Spesialis retrieved successfully")

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody request: SpesialisReq): BaseResponse<Nothing?> {
        service.save(request)
        return null.isSuccess("Spesialis created successfully", 201)
    }

    @PatchMapping("/{id}")
    fun update(@PathVariable id: UUID, @RequestBody request: SpesialisReq): BaseResponse<Nothing?> {
        service.update(request, id)
        return null.isSuccess("Spesialis updated successfully")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): BaseResponse<Nothing?> {
        service.delete(id)
        return null.isSuccess("Spesialis deleted successfully")
    }
}
