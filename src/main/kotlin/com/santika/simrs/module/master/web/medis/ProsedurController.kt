package com.santika.simrs.module.master.web.medis

import com.santika.simrs.global.response.BaseResponse
import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.global.response.PageResponse
import com.santika.simrs.global.response.SliceResponse
import com.santika.simrs.global.response.toSliceResponse
import com.santika.simrs.global.response.Response.isSuccess
import com.santika.simrs.module.master.dto.request.MedisProsedurReq
import com.santika.simrs.module.master.dto.response.MedisProsedurRes
import com.santika.simrs.module.master.service.MedisProsedurService
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/master/medis/prosedur")
class ProsedurController(private val service: MedisProsedurService) {

    @GetMapping
    fun findAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?
    ): BaseResponse<PageResponse<MedisProsedurRes>> {
        val result = service.findAll(PageRequest.of(page, size), search)
        return PageResponse.of(result.map { MedisProsedurRes.ofProsedur(it) }).isSuccess("OK")
    }

    @GetMapping("/options")
    fun options(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) search: String?
    ): BaseResponse<SliceResponse<OptionsRes>> =
        service.options(PageRequest.of(page, size), search).toSliceResponse().isSuccess("OK")

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): BaseResponse<MedisProsedurRes> =
        MedisProsedurRes.ofProsedur(service.findById(id)).isSuccess("OK")

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody request: MedisProsedurReq): BaseResponse<Nothing?> {
        service.save(request)
        return null.isSuccess("Berhasil disimpan", 201)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @RequestBody request: MedisProsedurReq): BaseResponse<Nothing?> {
        service.update(request, id)
        return null.isSuccess("Berhasil diupdate")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): BaseResponse<Nothing?> {
        service.delete(id)
        return null.isSuccess("Berhasil dihapus")
    }
}
