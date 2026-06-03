package com.santika.simrs.module.kepegawaian.service

import com.santika.simrs.global.exception.DataNotFoundException
import com.santika.simrs.module.kepegawaian.dto.request.DepartemenPegawaiReq
import com.santika.simrs.module.kepegawaian.infrastructure.entities.DepartemenPegawaiEntity
import com.santika.simrs.module.kepegawaian.infrastructure.repository.DepartemenPegawaiRepo
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DepartemenPegawaiService(private val repo: DepartemenPegawaiRepo) {

    fun findAll(pageable: Pageable, search: String?) = repo.findAllBySearch(pageable, search)
    fun options(pageable: Pageable, search: String?) = repo.findOptions(pageable, search)

    fun findById(id: UUID): DepartemenPegawaiEntity =
        repo.findById(id).orElseThrow { DataNotFoundException("Departemen pegawai tidak ditemukan") }

    @Transactional
    fun save(request: DepartemenPegawaiReq) {
        repo.save(DepartemenPegawaiEntity().apply {
            kodeDepartemen = request.kodeDepartemen
            namaDepartemen = request.namaDepartemen
        })
    }

    @Transactional
    fun update(request: DepartemenPegawaiReq, id: UUID) {
        val data = findById(id)
        data.apply {
            kodeDepartemen = request.kodeDepartemen
            namaDepartemen = request.namaDepartemen
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
