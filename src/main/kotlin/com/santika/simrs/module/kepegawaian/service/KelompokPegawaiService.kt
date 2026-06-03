package com.santika.simrs.module.kepegawaian.service

import com.santika.simrs.global.exception.DataNotFoundException
import com.santika.simrs.module.kepegawaian.dto.request.KelompokPegawaiReq
import com.santika.simrs.module.kepegawaian.infrastructure.entities.KelompokPegawaiEntity
import com.santika.simrs.module.kepegawaian.infrastructure.repository.KelompokPegawaiRepo
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class KelompokPegawaiService(private val repo: KelompokPegawaiRepo) {

    fun findAll(pageable: Pageable, search: String?) = repo.findAllBySearch(pageable, search)
    fun options(pageable: Pageable, search: String?) = repo.findOptions(pageable, search)

    fun findById(id: UUID): KelompokPegawaiEntity =
        repo.findById(id).orElseThrow { DataNotFoundException("Kelompok pegawai tidak ditemukan") }

    @Transactional
    fun save(request: KelompokPegawaiReq) {
        repo.save(KelompokPegawaiEntity().apply {
            kodeKelompok = request.kodeKelompok
            namaKelompok = request.namaKelompok
        })
    }

    @Transactional
    fun update(request: KelompokPegawaiReq, id: UUID) {
        val data = findById(id)
        data.apply {
            kodeKelompok = request.kodeKelompok
            namaKelompok = request.namaKelompok
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
