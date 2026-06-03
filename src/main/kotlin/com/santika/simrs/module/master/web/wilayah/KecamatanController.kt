package com.santika.simrs.module.master.web.wilayah

import com.santika.simrs.global.response.BaseResponse
import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.global.response.PageResponse
import com.santika.simrs.global.response.Response.isSuccess
import com.santika.simrs.global.response.SliceResponse
import com.santika.simrs.global.response.toSliceResponse
import com.santika.simrs.module.master.dto.request.WilayahKecamatanReq
import com.santika.simrs.module.master.dto.response.WilayahKecamatanRes
import com.santika.simrs.module.master.service.WilayahKecamatanService
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/master/wilayah/kecamatan")
class KecamatanController(private val service: WilayahKecamatanService) {

    @GetMapping
    fun findAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?,
        @RequestParam(required = false) kabKotaId: UUID?
    ): BaseResponse<PageResponse<WilayahKecamatanRes>> {
        val result = service.findAll(PageRequest.of(page, size), kabKotaId, search)
        return PageResponse.of(result.map { WilayahKecamatanRes.ofKecamatan(it) }).isSuccess("OK")
    }

    @GetMapping("/options")
    fun options(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) search: String?
    ): BaseResponse<SliceResponse<OptionsRes>> =
        service.options(PageRequest.of(page, size), search).toSliceResponse().isSuccess("OK")

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): BaseResponse<WilayahKecamatanRes> =
        WilayahKecamatanRes.ofKecamatan(service.findById(id)).isSuccess("OK")

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody request: WilayahKecamatanReq): BaseResponse<Nothing?> {
        service.save(request)
        return null.isSuccess("Berhasil disimpan", 201)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @RequestBody request: WilayahKecamatanReq): BaseResponse<Nothing?> {
        service.update(request, id)
        return null.isSuccess("Berhasil diupdate")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): BaseResponse<Nothing?> {
        service.delete(id)
        return null.isSuccess("Berhasil dihapus")
    }
}
