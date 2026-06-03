package com.santika.simrs.module.master.web.medis

import com.santika.simrs.global.response.BaseResponse
import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.global.response.PageResponse
import com.santika.simrs.global.response.SliceResponse
import com.santika.simrs.global.response.toSliceResponse
import com.santika.simrs.global.response.Response.isSuccess
import com.santika.simrs.module.master.dto.request.MedisDiagnosaReq
import com.santika.simrs.module.master.dto.response.MedisDiagnosaRes
import com.santika.simrs.module.master.service.MedisDiagnosaService
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/master/medis/diagnosa")
class DiagnosaController(private val service: MedisDiagnosaService) {

    @GetMapping
    fun findAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?
    ): BaseResponse<PageResponse<MedisDiagnosaRes>> {
        val result = service.findAll(PageRequest.of(page, size), search)
        return PageResponse.of(result.map { MedisDiagnosaRes.ofDiagnosa(it) }).isSuccess("OK")
    }

    @GetMapping("/options")
    fun options(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) search: String?
    ): BaseResponse<SliceResponse<OptionsRes>> =
        service.options(PageRequest.of(page, size), search).toSliceResponse().isSuccess("OK")

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): BaseResponse<MedisDiagnosaRes> =
        MedisDiagnosaRes.ofDiagnosa(service.findById(id)).isSuccess("OK")

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody request: MedisDiagnosaReq): BaseResponse<Nothing?> {
        service.save(request)
        return null.isSuccess("Berhasil disimpan", 201)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @RequestBody request: MedisDiagnosaReq): BaseResponse<Nothing?> {
        service.update(request, id)
        return null.isSuccess("Berhasil diupdate")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): BaseResponse<Nothing?> {
        service.delete(id)
        return null.isSuccess("Berhasil dihapus")
    }
}
