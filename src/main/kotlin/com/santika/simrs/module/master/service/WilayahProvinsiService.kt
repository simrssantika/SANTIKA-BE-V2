package com.santika.simrs.module.master.service

import com.santika.simrs.global.exception.DataNotFoundException
import com.santika.simrs.module.master.dto.request.WilayahProvinsiReq
import com.santika.simrs.module.master.infrastructure.entities.WilayahProvinsiEntity
import com.santika.simrs.module.master.infrastructure.repository.WilayahProvinsiRepo
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class WilayahProvinsiService(private val provinsiRepo: WilayahProvinsiRepo) {

    fun findAll(pageable: Pageable, search: String?) = provinsiRepo.findAllBySearch(pageable, search)
    fun options(pageable: Pageable, search: String?) = provinsiRepo.findOptions(pageable, search)

    fun findById(id: UUID): WilayahProvinsiEntity =
        provinsiRepo.findById(id).orElseThrow { DataNotFoundException("Provinsi tidak ditemukan") }

    @Transactional
    fun save(request: WilayahProvinsiReq) {
        provinsiRepo.save(WilayahProvinsiEntity().apply {
            kodeProvinsi = request.kodeProvinsi
            namaProvinsi = request.namaProvinsi
        })
    }

    @Transactional
    fun update(request: WilayahProvinsiReq, id: UUID) {
        val data = findById(id)
        data.apply {
            kodeProvinsi = request.kodeProvinsi
            namaProvinsi = request.namaProvinsi
        }
        provinsiRepo.save(data)
    }

    @Transactional
    fun delete(id: UUID) {
        val data = findById(id)
        data.softDelete()
        provinsiRepo.save(data)
    }
}
