package com.santika.simrs.module.kepegawaian.service

import com.santika.simrs.global.exception.DataNotFoundException
import com.santika.simrs.module.kepegawaian.dto.request.ResikoPegawaiReq
import com.santika.simrs.module.kepegawaian.infrastructure.entities.ResikoPegawaiEntity
import com.santika.simrs.module.kepegawaian.infrastructure.repository.ResikoPegawaiRepo
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ResikoPegawaiService(private val repo: ResikoPegawaiRepo) {

    fun findAll(pageable: Pageable, search: String?) = repo.findAllBySearch(pageable, search)
    fun options(pageable: Pageable, search: String?) = repo.findOptions(pageable, search)

    fun findById(id: UUID): ResikoPegawaiEntity =
        repo.findById(id).orElseThrow { DataNotFoundException("Resiko pegawai tidak ditemukan") }

    @Transactional
    fun save(request: ResikoPegawaiReq) {
        repo.save(ResikoPegawaiEntity().apply {
            kodeResiko = request.kodeResiko
            namaResiko = request.namaResiko
        })
    }

    @Transactional
    fun update(request: ResikoPegawaiReq, id: UUID) {
        val data = findById(id)
        data.apply {
            kodeResiko = request.kodeResiko
            namaResiko = request.namaResiko
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
