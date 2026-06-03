package com.santika.simrs.module.master.service

import com.santika.simrs.global.exception.DataNotFoundException
import com.santika.simrs.module.master.dto.request.MedisDiagnosaReq
import com.santika.simrs.module.master.infrastructure.entities.MedisDiagnosaEntity
import com.santika.simrs.module.master.infrastructure.repository.MedisDiagnosaRepo
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class MedisDiagnosaService(private val medisDiagnosaRepo: MedisDiagnosaRepo) {

    fun findAll(pageable: Pageable, search: String?) = medisDiagnosaRepo.findAllBySearch(pageable, search)
    fun options(pageable: Pageable, search: String?) = medisDiagnosaRepo.findOptions(pageable, search)

    fun findById(id: UUID): MedisDiagnosaEntity =
        medisDiagnosaRepo.findById(id).orElseThrow { DataNotFoundException("Diagnosa tidak ditemukan") }

    @Transactional
    fun save(request: MedisDiagnosaReq) {
        medisDiagnosaRepo.save(MedisDiagnosaEntity().apply {
            kodeDiagnosa = request.kodeDiagnosa
            namaDiagnosa = request.namaDiagnosa
        })
    }

    @Transactional
    fun update(request: MedisDiagnosaReq, id: UUID) {
        val data = findById(id)
        data.apply {
            kodeDiagnosa = request.kodeDiagnosa
            namaDiagnosa = request.namaDiagnosa
        }
        medisDiagnosaRepo.save(data)
    }

    @Transactional
    fun delete(id: UUID) {
        val data = findById(id)
        data.softDelete()
        medisDiagnosaRepo.save(data)
    }
}
