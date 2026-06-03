package com.santika.simrs.module.kepegawaian.service

import com.santika.simrs.global.exception.DataNotFoundException
import com.santika.simrs.module.kepegawaian.dto.request.BidangPegawaiReq
import com.santika.simrs.module.kepegawaian.infrastructure.entities.BidangPegawaiEntity
import com.santika.simrs.module.kepegawaian.infrastructure.repository.BidangPegawaiRepo
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class BidangPegawaiService(private val repo: BidangPegawaiRepo) {

    fun findAll(pageable: Pageable, search: String?) = repo.findAllBySearch(pageable, search)
    fun options(pageable: Pageable, search: String?) = repo.findOptions(pageable, search)

    fun findById(id: UUID): BidangPegawaiEntity =
        repo.findById(id).orElseThrow { DataNotFoundException("Bidang pegawai tidak ditemukan") }

    @Transactional
    fun save(request: BidangPegawaiReq) {
        repo.save(BidangPegawaiEntity().apply {
            kodeBidang = request.kodeBidang
            namaBidang = request.namaBidang
        })
    }

    @Transactional
    fun update(request: BidangPegawaiReq, id: UUID) {
        val data = findById(id)
        data.apply {
            kodeBidang = request.kodeBidang
            namaBidang = request.namaBidang
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
