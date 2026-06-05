package com.santika.simrs.module.kepegawaian.service

import com.santika.simrs.global.exception.DataNotFoundException
import com.santika.simrs.module.kepegawaian.dto.request.PegawaiReq
import com.santika.simrs.module.kepegawaian.dto.response.PegawaiDetailRes
import com.santika.simrs.module.kepegawaian.dto.response.PegawaiListRes
import com.santika.simrs.module.kepegawaian.infrastructure.entities.PegawaiEntity
import com.santika.simrs.module.kepegawaian.infrastructure.jooq.PegawaiJooqRepo
import com.santika.simrs.module.kepegawaian.infrastructure.mapper.applyFrom
import com.santika.simrs.module.kepegawaian.infrastructure.repository.PegawaiRepo
import com.santika.simrs.shared.file.temp.TempFileService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class PegawaiService(
    private val repo: PegawaiRepo,
    private val jooqRepo: PegawaiJooqRepo,
    private val tempFileService: TempFileService,
    private val dokumenPegawaiService: DokumenPegawaiService,
    private val dokterService: DokterService
) {

    fun findAll(
        pageable: Pageable,
        search: String?,
        isActive: Boolean?,
        statusPegawai: String?,
        departemenId: UUID?
    ): Slice<PegawaiListRes> = jooqRepo.findAll(pageable, search, isActive, statusPegawai, departemenId)

    fun options(pageable: Pageable, search: String?) = jooqRepo.findOptions(pageable, search)

    fun findById(id: UUID): PegawaiDetailRes {
        // Double query (N+1 dihalalkan untuk read single): pegawai + dokter terpisah.
        val pegawai = jooqRepo.findById(id) ?: throw DataNotFoundException("Pegawai tidak ditemukan")
        return pegawai.copy(dokter = dokterService.findByPegawaiId(id))
    }

    private fun findEntityById(id: UUID): PegawaiEntity =
        repo.findById(id).orElseThrow { DataNotFoundException("Pegawai tidak ditemukan") }

    @Transactional
    fun save(request: PegawaiReq) {
        // 1. Simpan pegawai dulu untuk mendapat id (foto di-set setelah file dipindah)
        val pegawai = PegawaiEntity().applyFrom(request)
        pegawai.fotoId = null
        repo.save(pegawai)
        val pegawaiId = pegawai.id!!

        // 2. Foto: pindahkan temp → storage lalu simpan storage id
        request.fotoId?.let { tempId ->
            val stored = tempFileService.moveToStorage(
                listOf(tempId), "kepegawaian:pegawai_foto", pegawaiId
            )
            pegawai.fotoId = stored.first().id
            repo.save(pegawai)
        }

        // 3. Dokumen & dokter: persistensi tetap di service masing-masing.
        //    Propagation REQUIRED → ikut transaksi ini (gagal di mana pun = rollback semua).
        request.dokumen?.let { dokumenPegawaiService.saveAll(pegawaiId, it) }
        request.dokter?.let { dokterService.save(it.copy(pegawaiId = pegawaiId)) }
    }

    @Transactional
    fun update(request: PegawaiReq, id: UUID) {
        val data = findEntityById(id)
        val oldFotoId = data.fotoId
        data.applyFrom(request)

        tempFileService.updateFile(
            tempIds = listOfNotNull(request.fotoId),
            oldTempIds = listOfNotNull(oldFotoId),
        ) { storedFiles ->
            storedFiles.map {
                data.fotoId = it.id
            }
        }

        repo.save(data)
    }

    @Transactional
    fun delete(id: UUID) {
        val data = findEntityById(id)
        data.softDelete()
        repo.save(data)
    }
}
