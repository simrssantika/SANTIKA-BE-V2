package com.santika.simrs.module.kepegawaian.web

import com.santika.simrs.global.response.BaseResponse
import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.global.response.PageResponse
import com.santika.simrs.global.response.Response.isSuccess
import com.santika.simrs.global.response.SliceResponse
import com.santika.simrs.global.response.toSliceResponse
import com.santika.simrs.module.kepegawaian.dto.request.VendorTenagaLuarReq
import com.santika.simrs.module.kepegawaian.dto.response.VendorTenagaLuarRes
import com.santika.simrs.module.kepegawaian.service.VendorTenagaLuarService
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/kepegawaian/vendor")
class VendorTenagaLuarController(private val service: VendorTenagaLuarService) {

    @GetMapping
    fun findAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?
    ): BaseResponse<PageResponse<VendorTenagaLuarRes>> {
        val result = service.findAll(PageRequest.of(page, size), search)
        return PageResponse.of(result.map { VendorTenagaLuarRes.of(it) }).isSuccess("Vendor retrieved successfully")
    }

    @GetMapping("/options")
    fun options(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) search: String?
    ): BaseResponse<SliceResponse<OptionsRes>> =
        service.options(PageRequest.of(page, size), search).toSliceResponse().isSuccess("OK")

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): BaseResponse<VendorTenagaLuarRes> =
        VendorTenagaLuarRes.of(service.findById(id)).isSuccess("Vendor retrieved successfully")

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody request: VendorTenagaLuarReq): BaseResponse<Nothing?> {
        service.save(request)
        return null.isSuccess("Vendor created successfully", 201)
    }

    @PatchMapping("/{id}")
    fun update(@PathVariable id: UUID, @RequestBody request: VendorTenagaLuarReq): BaseResponse<Nothing?> {
        service.update(request, id)
        return null.isSuccess("Vendor updated successfully")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): BaseResponse<Nothing?> {
        service.delete(id)
        return null.isSuccess("Vendor deleted successfully")
    }
}
