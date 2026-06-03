package com.santika.simrs.module.kepegawaian.service

import com.santika.simrs.global.exception.DataNotFoundException
import com.santika.simrs.module.kepegawaian.dto.request.DokumenPegawaiReq
import com.santika.simrs.module.kepegawaian.infrastructure.entities.DokumenPegawaiEntity
import com.santika.simrs.module.kepegawaian.infrastructure.repository.DokumenPegawaiRepo
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DokumenPegawaiService(private val repo: DokumenPegawaiRepo) {

    fun findAllByPegawai(pageable: Pageable, pegawaiId: UUID, search: String?) =
        repo.findAllByPegawaiId(pageable, pegawaiId, search)

    fun findById(id: UUID): DokumenPegawaiEntity =
        repo.findById(id).orElseThrow { DataNotFoundException("Dokumen pegawai tidak ditemukan") }

    @Transactional
    fun save(pegawaiId: UUID, request: DokumenPegawaiReq) {
        repo.save(DokumenPegawaiEntity().apply {
            this.pegawaiId = pegawaiId
            namaDokumen = request.namaDokumen
            fileId = request.fileId
            isActive = request.isActive
        })
    }

    @Transactional
    fun update(request: DokumenPegawaiReq, id: UUID) {
        val data = findById(id)
        data.apply {
            namaDokumen = request.namaDokumen
            fileId = request.fileId
            isActive = request.isActive
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
