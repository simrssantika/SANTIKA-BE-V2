package com.santika.simrs.module.master.web.wilayah

import com.santika.simrs.global.response.BaseResponse
import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.global.response.PageResponse
import com.santika.simrs.global.response.SliceResponse
import com.santika.simrs.global.response.toSliceResponse
import com.santika.simrs.global.response.Response.isSuccess
import com.santika.simrs.module.master.dto.request.WilayahKabKotaReq
import com.santika.simrs.module.master.dto.response.WilayahKabKotaRes
import com.santika.simrs.module.master.service.WilayahKabKotaService
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/master/wilayah/kab-kota")
class KabKotaController(private val service: WilayahKabKotaService) {

    @GetMapping
    fun findAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?,
        @RequestParam(required = false) provinsiId: UUID?
    ): BaseResponse<PageResponse<WilayahKabKotaRes>> {
        val result = service.findAll(PageRequest.of(page, size), provinsiId, search)
        return PageResponse.of(result.map { WilayahKabKotaRes.ofKabKota(it) }).isSuccess("OK")
    }

    @GetMapping("/options")
    fun options(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) search: String?
    ): BaseResponse<SliceResponse<OptionsRes>> =
        service.options(PageRequest.of(page, size), search).toSliceResponse().isSuccess("OK")

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): BaseResponse<WilayahKabKotaRes> =
        WilayahKabKotaRes.ofKabKota(service.findById(id)).isSuccess("OK")

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody request: WilayahKabKotaReq): BaseResponse<Nothing?> {
        service.save(request)
        return null.isSuccess("Berhasil disimpan", 201)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @RequestBody request: WilayahKabKotaReq): BaseResponse<Nothing?> {
        service.update(request, id)
        return null.isSuccess("Berhasil diupdate")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): BaseResponse<Nothing?> {
        service.delete(id)
        return null.isSuccess("Berhasil dihapus")
    }
}
