package com.santika.simrs.module.master.service

import com.santika.simrs.global.exception.DataNotFoundException
import com.santika.simrs.module.master.dto.request.SupportPenjaminReq
import com.santika.simrs.module.master.infrastructure.entities.SupportPenjaminEntity
import com.santika.simrs.module.master.infrastructure.repository.SupportPenjaminRepo
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class SupportPenjaminService(private val supportPenjaminRepo: SupportPenjaminRepo) {

    fun findAll(pageable: Pageable, search: String?, isActive: Boolean?) =
        supportPenjaminRepo.findAllBySearch(pageable, search, isActive)
    fun options(pageable: Pageable, search: String?) = supportPenjaminRepo.findOptions(pageable, search)

    fun findById(id: UUID): SupportPenjaminEntity =
        supportPenjaminRepo.findById(id).orElseThrow { DataNotFoundException("Penjamin tidak ditemukan") }

    @Transactional
    fun save(request: SupportPenjaminReq) {
        supportPenjaminRepo.save(SupportPenjaminEntity().apply {
            penjamin = request.penjamin
            namaPerusahaanPenjamin = request.namaPerusahaanPenjamin
            alamatPenjamin = request.alamatPenjamin
            teleponPenjamin = request.telephonePenjamin
            penerimaTagihan = request.penerimaTagihan
            isActive = request.isActive
        })
    }

    @Transactional
    fun update(request: SupportPenjaminReq, id: UUID) {
        val data = findById(id)
        data.apply {
            penjamin = request.penjamin
            namaPerusahaanPenjamin = request.namaPerusahaanPenjamin
            alamatPenjamin = request.alamatPenjamin
            teleponPenjamin = request.telephonePenjamin
            penerimaTagihan = request.penerimaTagihan
            isActive = request.isActive
        }
        supportPenjaminRepo.save(data)
    }

    @Transactional
    fun delete(id: UUID) {
        val data = findById(id)
        data.softDelete()
        supportPenjaminRepo.save(data)
    }
}
