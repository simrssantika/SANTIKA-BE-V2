package com.santika.simrs.module.kepegawaian.service

import com.santika.simrs.global.exception.DataNotFoundException
import com.santika.simrs.module.kepegawaian.dto.request.VendorTenagaLuarReq
import com.santika.simrs.module.kepegawaian.infrastructure.entities.VendorTenagaLuarEntity
import com.santika.simrs.module.kepegawaian.infrastructure.repository.VendorTenagaLuarRepo
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class VendorTenagaLuarService(private val repo: VendorTenagaLuarRepo) {

    fun findAll(pageable: Pageable, search: String?) = repo.findAllBySearch(pageable, search)
    fun options(pageable: Pageable, search: String?) = repo.findOptions(pageable, search)

    fun findById(id: UUID): VendorTenagaLuarEntity =
        repo.findById(id).orElseThrow { DataNotFoundException("Vendor tenaga luar tidak ditemukan") }

    @Transactional
    fun save(request: VendorTenagaLuarReq) {
        repo.save(VendorTenagaLuarEntity().apply {
            namaVendor = request.namaVendor
            alamatVendor = request.alamatVendor
            bidangVendor = request.bidangVendor
            telephoneVendor = request.telephoneVendor
            namaKontakDarurat = request.namaKontakDarurat
            kontakDaruratVendor = request.kontakDaruratVendor
        })
    }

    @Transactional
    fun update(request: VendorTenagaLuarReq, id: UUID) {
        val data = findById(id)
        data.apply {
            namaVendor = request.namaVendor
            alamatVendor = request.alamatVendor
            bidangVendor = request.bidangVendor
            telephoneVendor = request.telephoneVendor
            namaKontakDarurat = request.namaKontakDarurat
            kontakDaruratVendor = request.kontakDaruratVendor
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
