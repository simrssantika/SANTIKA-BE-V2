package com.santika.simrs.module.master.service

import com.santika.simrs.global.exception.DataNotFoundException
import com.santika.simrs.module.master.dto.request.MedisProsedurReq
import com.santika.simrs.module.master.infrastructure.entities.MedisProsedurEntity
import com.santika.simrs.module.master.infrastructure.repository.MedisProsedurRepo
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class MedisProsedurService(private val medisProsedurRepo: MedisProsedurRepo) {

    fun findAll(pageable: Pageable, search: String?) = medisProsedurRepo.findAllBySearch(pageable, search)
    fun options(pageable: Pageable, search: String?) = medisProsedurRepo.findOptions(pageable, search)

    fun findById(id: UUID): MedisProsedurEntity =
        medisProsedurRepo.findById(id).orElseThrow { DataNotFoundException("Prosedur tidak ditemukan") }

    @Transactional
    fun save(request: MedisProsedurReq) {
        medisProsedurRepo.save(MedisProsedurEntity().apply {
            kodeProsedur = request.kodeProsedur
            namaProsedur = request.namaProsedur
        })
    }

    @Transactional
    fun update(request: MedisProsedurReq, id: UUID) {
        val data = findById(id)
        data.apply {
            kodeProsedur = request.kodeProsedur
            namaProsedur = request.namaProsedur
        }
        medisProsedurRepo.save(data)
    }

    @Transactional
    fun delete(id: UUID) {
        val data = findById(id)
        data.softDelete()
        medisProsedurRepo.save(data)
    }
}
