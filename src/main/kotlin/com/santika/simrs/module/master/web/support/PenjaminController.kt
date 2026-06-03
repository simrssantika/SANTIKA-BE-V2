package com.santika.simrs.module.master.web.support

import com.santika.simrs.global.response.BaseResponse
import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.global.response.PageResponse
import com.santika.simrs.global.response.SliceResponse
import com.santika.simrs.global.response.toSliceResponse
import com.santika.simrs.global.response.Response.isSuccess
import com.santika.simrs.module.master.dto.request.SupportPenjaminReq
import com.santika.simrs.module.master.dto.response.SupportPenjaminRes
import com.santika.simrs.module.master.service.SupportPenjaminService
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/master/support/penjamin")
class PenjaminController(private val service: SupportPenjaminService) {

    @GetMapping
    fun findAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?,
        @RequestParam(required = false) isActive: Boolean?
    ): BaseResponse<PageResponse<SupportPenjaminRes>> {
        val result = service.findAll(PageRequest.of(page, size), search, isActive)
        return PageResponse.of(result.map { SupportPenjaminRes.ofPenjamin(it) }).isSuccess("OK")
    }

    @GetMapping("/options")
    fun options(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) search: String?
    ): BaseResponse<SliceResponse<OptionsRes>> =
        service.options(PageRequest.of(page, size), search).toSliceResponse().isSuccess("OK")

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): BaseResponse<SupportPenjaminRes> =
        SupportPenjaminRes.ofPenjamin(service.findById(id)).isSuccess("OK")

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody request: SupportPenjaminReq): BaseResponse<Nothing?> {
        service.save(request)
        return null.isSuccess("Berhasil disimpan", 201)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @RequestBody request: SupportPenjaminReq): BaseResponse<Nothing?> {
        service.update(request, id)
        return null.isSuccess("Berhasil diupdate")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): BaseResponse<Nothing?> {
        service.delete(id)
        return null.isSuccess("Berhasil dihapus")
    }
}
