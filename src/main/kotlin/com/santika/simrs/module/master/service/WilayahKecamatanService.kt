package com.santika.simrs.module.master.service

import com.santika.simrs.global.exception.DataNotFoundException
import com.santika.simrs.module.master.dto.request.WilayahKecamatanReq
import com.santika.simrs.module.master.infrastructure.entities.WilayahKecamatanEntity
import com.santika.simrs.module.master.infrastructure.repository.WilayahKabKotaRepo
import com.santika.simrs.module.master.infrastructure.repository.WilayahKecamatanRepo
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class WilayahKecamatanService(
    private val kecamatanRepo: WilayahKecamatanRepo,
    private val kabKotaRepo: WilayahKabKotaRepo
) {

    fun findAll(pageable: Pageable, kabKotaId: UUID?, search: String?) =
        kecamatanRepo.findAllByKabKotaIdAndSearch(pageable, kabKotaId, search)
    fun options(pageable: Pageable, search: String?) = kecamatanRepo.findOptions(pageable, search)

    fun findById(id: UUID): WilayahKecamatanEntity =
        kecamatanRepo.findById(id).orElseThrow { DataNotFoundException("Kecamatan tidak ditemukan") }

    @Transactional
    fun save(request: WilayahKecamatanReq) {
        if (!kabKotaRepo.existsById(request.kabKotaId))
            throw IllegalArgumentException("Kab/Kota tidak ditemukan")
        kecamatanRepo.save(WilayahKecamatanEntity().apply {
            kabKotaId = request.kabKotaId
            kodeKecamatan = request.kodeKecamatan
            namaKecamatan = request.namaKecamatan
        })
    }

    @Transactional
    fun update(request: WilayahKecamatanReq, id: UUID) {
        if (!kabKotaRepo.existsById(request.kabKotaId))
            throw IllegalArgumentException("Kab/Kota tidak ditemukan")
        val data = findById(id)
        data.apply {
            kabKotaId = request.kabKotaId
            kodeKecamatan = request.kodeKecamatan
            namaKecamatan = request.namaKecamatan
        }
        kecamatanRepo.save(data)
    }

    @Transactional
    fun delete(id: UUID) {
        val data = findById(id)
        data.softDelete()
        kecamatanRepo.save(data)
    }
}
