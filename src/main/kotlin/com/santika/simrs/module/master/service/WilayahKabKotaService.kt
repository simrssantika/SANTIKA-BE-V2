package com.santika.simrs.module.master.service

import com.santika.simrs.global.exception.DataNotFoundException
import com.santika.simrs.module.master.dto.request.WilayahKabKotaReq
import com.santika.simrs.module.master.infrastructure.entities.WilayahKabKotaEntity
import com.santika.simrs.module.master.infrastructure.repository.WilayahKabKotaRepo
import com.santika.simrs.module.master.infrastructure.repository.WilayahProvinsiRepo
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class WilayahKabKotaService(
    private val kabKotaRepo: WilayahKabKotaRepo,
    private val provinsiRepo: WilayahProvinsiRepo
) {

    fun findAll(pageable: Pageable, provinsiId: UUID?, search: String?) =
        kabKotaRepo.findAllByProvinsiIdOrSearch(pageable, provinsiId, search)
    fun options(pageable: Pageable, search: String?) = kabKotaRepo.findOptions(pageable, search)

    fun findById(id: UUID): WilayahKabKotaEntity =
        kabKotaRepo.findById(id).orElseThrow { DataNotFoundException("Kab/Kota tidak ditemukan") }

    @Transactional
    fun save(request: WilayahKabKotaReq) {
        if (!provinsiRepo.existsById(request.provinsiId))
            throw IllegalArgumentException("Provinsi tidak ditemukan")
        kabKotaRepo.save(WilayahKabKotaEntity().apply {
            provinsiId = request.provinsiId
            kodeKabKota = request.kodeKabKota
            namaKabKota = request.namaKabKota
        })
    }

    @Transactional
    fun update(request: WilayahKabKotaReq, id: UUID) {
        if (!provinsiRepo.existsById(request.provinsiId))
            throw IllegalArgumentException("Provinsi tidak ditemukan")
        val data = findById(id)
        data.apply {
            provinsiId = request.provinsiId
            kodeKabKota = request.kodeKabKota
            namaKabKota = request.namaKabKota
        }
        kabKotaRepo.save(data)
    }

    @Transactional
    fun delete(id: UUID) {
        val data = findById(id)
        data.softDelete()
        kabKotaRepo.save(data)
    }
}
