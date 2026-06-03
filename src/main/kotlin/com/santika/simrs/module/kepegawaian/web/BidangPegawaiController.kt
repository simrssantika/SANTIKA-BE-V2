package com.santika.simrs.module.kepegawaian.web

import com.santika.simrs.global.response.BaseResponse
import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.global.response.PageResponse
import com.santika.simrs.global.response.Response.isSuccess
import com.santika.simrs.global.response.SliceResponse
import com.santika.simrs.global.response.toSliceResponse
import com.santika.simrs.module.kepegawaian.dto.request.BidangPegawaiReq
import com.santika.simrs.module.kepegawaian.dto.response.BidangPegawaiRes
import com.santika.simrs.module.kepegawaian.service.BidangPegawaiService
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/kepegawaian/bidang")
class BidangPegawaiController(private val service: BidangPegawaiService) {

    @GetMapping
    fun findAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?
    ): BaseResponse<PageResponse<BidangPegawaiRes>> {
        val result = service.findAll(PageRequest.of(page, size), search)
        return PageResponse.of(result.map { BidangPegawaiRes.of(it) }).isSuccess("Bidang retrieved successfully")
    }

    @GetMapping("/options")
    fun options(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) search: String?
    ): BaseResponse<SliceResponse<OptionsRes>> =
        service.options(PageRequest.of(page, size), search).toSliceResponse().isSuccess("OK")

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): BaseResponse<BidangPegawaiRes> =
        BidangPegawaiRes.of(service.findById(id)).isSuccess("Bidang retrieved successfully")

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody request: BidangPegawaiReq): BaseResponse<Nothing?> {
        service.save(request)
        return null.isSuccess("Bidang created successfully", 201)
    }

    @PatchMapping("/{id}")
    fun update(@PathVariable id: UUID, @RequestBody request: BidangPegawaiReq): BaseResponse<Nothing?> {
        service.update(request, id)
        return null.isSuccess("Bidang updated successfully")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): BaseResponse<Nothing?> {
        service.delete(id)
        return null.isSuccess("Bidang deleted successfully")
    }
}
