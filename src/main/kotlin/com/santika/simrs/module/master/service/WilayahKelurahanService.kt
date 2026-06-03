package com.santika.simrs.module.master.service

import com.santika.simrs.global.exception.DataNotFoundException
import com.santika.simrs.module.master.dto.request.WilayahKelurahanReq
import com.santika.simrs.module.master.infrastructure.entities.WilayahKelurahanEntity
import com.santika.simrs.module.master.infrastructure.repository.WilayahKecamatanRepo
import com.santika.simrs.module.master.infrastructure.repository.WilayahKelurahanRepo
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class WilayahKelurahanService(
    private val kelurahanRepo: WilayahKelurahanRepo,
    private val kecamatanRepo: WilayahKecamatanRepo
) {

    fun findAll(pageable: Pageable, kecamatanId: UUID?, search: String?) =
        kelurahanRepo.findAllByKecamatanIdAndSearch(pageable, kecamatanId, search)
    fun options(pageable: Pageable, search: String?) = kelurahanRepo.findOptions(pageable, search)

    fun findById(id: UUID): WilayahKelurahanEntity =
        kelurahanRepo.findById(id).orElseThrow { DataNotFoundException("Kelurahan tidak ditemukan") }

    @Transactional
    fun save(request: WilayahKelurahanReq) {
        if (!kecamatanRepo.existsById(request.kecamatanId))
            throw IllegalArgumentException("Kecamatan tidak ditemukan")
        kelurahanRepo.save(WilayahKelurahanEntity().apply {
            kecamatanId = request.kecamatanId
            kodeKelurahan = request.kodeKelurahan
            namaKelurahan = request.namaKelurahan
        })
    }

    @Transactional
    fun update(request: WilayahKelurahanReq, id: UUID) {
        if (!kecamatanRepo.existsById(request.kecamatanId))
            throw IllegalArgumentException("Kecamatan tidak ditemukan")
        val data = findById(id)
        data.apply {
            kecamatanId = request.kecamatanId
            kodeKelurahan = request.kodeKelurahan
            namaKelurahan = request.namaKelurahan
        }
        kelurahanRepo.save(data)
    }

    @Transactional
    fun delete(id: UUID) {
        val data = findById(id)
        data.softDelete()
        kelurahanRepo.save(data)
    }
}
