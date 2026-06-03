package com.santika.simrs.module.kepegawaian.service

import com.santika.simrs.global.exception.DataNotFoundException
import com.santika.simrs.module.kepegawaian.dto.request.DokterReq
import com.santika.simrs.module.kepegawaian.dto.response.DokterDetailRes
import com.santika.simrs.module.kepegawaian.dto.response.DokterListRes
import com.santika.simrs.module.kepegawaian.infrastructure.entities.DokterEntity
import com.santika.simrs.module.kepegawaian.infrastructure.jooq.DokterJooqRepo
import com.santika.simrs.module.kepegawaian.infrastructure.mapper.applyFrom
import com.santika.simrs.module.kepegawaian.infrastructure.repository.DokterRepo
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class DokterService(
    private val repo: DokterRepo,
    private val jooqRepo: DokterJooqRepo
) {

    fun findAll(
        pageable: Pageable,
        search: String?,
        isActive: Boolean?,
        spesialisId: UUID?
    ): Slice<DokterListRes> = jooqRepo.findAll(pageable, search, isActive, spesialisId)
    fun options(pageable: Pageable, search: String?) = jooqRepo.findOptions(pageable, search)

    fun findById(id: UUID): DokterDetailRes =
        jooqRepo.findById(id) ?: throw DataNotFoundException("Dokter tidak ditemukan")

    private fun findEntityById(id: UUID): DokterEntity =
        repo.findById(id).orElseThrow { DataNotFoundException("Dokter tidak ditemukan") }

    @Transactional
    fun save(request: DokterReq) {
        repo.save(DokterEntity().applyFrom(request))
    }

    @Transactional
    fun update(request: DokterReq, id: UUID) {
        repo.save(findEntityById(id).applyFrom(request))
    }

    @Transactional
    fun delete(id: UUID) {
        val data = findEntityById(id)
        data.softDelete()
        repo.save(data)
    }
}
