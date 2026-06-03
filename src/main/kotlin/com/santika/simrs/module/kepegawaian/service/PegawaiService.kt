package com.santika.simrs.module.kepegawaian.service

import com.santika.simrs.global.exception.DataNotFoundException
import com.santika.simrs.module.kepegawaian.dto.request.PegawaiReq
import com.santika.simrs.module.kepegawaian.dto.response.PegawaiDetailRes
import com.santika.simrs.module.kepegawaian.dto.response.PegawaiListRes
import com.santika.simrs.module.kepegawaian.infrastructure.entities.PegawaiEntity
import com.santika.simrs.module.kepegawaian.infrastructure.jooq.PegawaiJooqRepo
import com.santika.simrs.module.kepegawaian.infrastructure.mapper.applyFrom
import com.santika.simrs.module.kepegawaian.infrastructure.repository.PegawaiRepo
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class PegawaiService(
    private val repo: PegawaiRepo,
    private val jooqRepo: PegawaiJooqRepo
) {

    fun findAll(
        pageable: Pageable,
        search: String?,
        isActive: Boolean?,
        statusPegawai: String?,
        departemenId: UUID?
    ): Slice<PegawaiListRes> = jooqRepo.findAll(pageable, search, isActive, statusPegawai, departemenId)
    fun options(pageable: Pageable, search: String?) = jooqRepo.findOptions(pageable, search)

    fun findById(id: UUID): PegawaiDetailRes =
        jooqRepo.findById(id) ?: throw DataNotFoundException("Pegawai tidak ditemukan")

    private fun findEntityById(id: UUID): PegawaiEntity =
        repo.findById(id).orElseThrow { DataNotFoundException("Pegawai tidak ditemukan") }

    @Transactional
    fun save(request: PegawaiReq) {
        repo.save(PegawaiEntity().applyFrom(request))
    }

    @Transactional
    fun update(request: PegawaiReq, id: UUID) {
        repo.save(findEntityById(id).applyFrom(request))
    }

    @Transactional
    fun delete(id: UUID) {
        val data = findEntityById(id)
        data.softDelete()
        repo.save(data)
    }
}
