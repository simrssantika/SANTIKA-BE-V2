package com.santika.simrs.module.kepegawaian.service

import com.santika.simrs.global.exception.DataNotFoundException
import com.santika.simrs.module.kepegawaian.dto.request.SpesialisReq
import com.santika.simrs.module.kepegawaian.infrastructure.entities.SpesialisEntity
import com.santika.simrs.module.kepegawaian.infrastructure.repository.SpesialisRepo
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class SpesialisService(private val repo: SpesialisRepo) {

    fun findAll(pageable: Pageable, search: String?) = repo.findAllBySearch(pageable, search)
    fun options(pageable: Pageable, search: String?) = repo.findOptions(pageable, search)

    fun findById(id: UUID): SpesialisEntity =
        repo.findById(id).orElseThrow { DataNotFoundException("Spesialis tidak ditemukan") }

    @Transactional
    fun save(request: SpesialisReq) {
        repo.save(SpesialisEntity().apply {
            kodeSpesialis = request.kodeSpesialis
            namaSpesialis = request.namaSpesialis
        })
    }

    @Transactional
    fun update(request: SpesialisReq, id: UUID) {
        val data = findById(id)
        data.apply {
            kodeSpesialis = request.kodeSpesialis
            namaSpesialis = request.namaSpesialis
        }
        repo.save(data)
    }

    @Transactional
    fun delete(id: UUID) {
        val data = findById(id)
        data.softDelete()
        repo.save(data)
    }
}
