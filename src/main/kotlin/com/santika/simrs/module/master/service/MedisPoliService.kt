package com.santika.simrs.module.master.service

import com.santika.simrs.global.exception.DataNotFoundException
import com.santika.simrs.module.master.dto.request.MedisPoliReq
import com.santika.simrs.module.master.infrastructure.entities.MedisPoliEntity
import com.santika.simrs.module.master.infrastructure.repository.MedisPoliRepo
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class MedisPoliService(private val medisPoliRepo: MedisPoliRepo) {

    fun findAll(pageable: Pageable, search: String?) = medisPoliRepo.findAllBySearch(pageable, search)
    fun options(pageable: Pageable, search: String?) = medisPoliRepo.findOptions(pageable, search)

    fun findById(id: UUID): MedisPoliEntity =
        medisPoliRepo.findById(id).orElseThrow { DataNotFoundException("Poli tidak ditemukan") }

    @Transactional
    fun save(request: MedisPoliReq) {
        medisPoliRepo.save(MedisPoliEntity().apply {
            kodePoli = request.kodePoli
            namaPoli = request.namaPoli
        })
    }

    @Transactional
    fun update(request: MedisPoliReq, id: UUID) {
        val data = findById(id)
        data.apply {
            kodePoli = request.kodePoli
            namaPoli = request.namaPoli
        }
        medisPoliRepo.save(data)
    }

    @Transactional
    fun delete(id: UUID) {
        val data = findById(id)
        data.softDelete()
        medisPoliRepo.save(data)
    }
}
