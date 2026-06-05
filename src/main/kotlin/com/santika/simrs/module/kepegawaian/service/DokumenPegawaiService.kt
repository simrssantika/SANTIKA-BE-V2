package com.santika.simrs.module.kepegawaian.service

import com.santika.simrs.module.kepegawaian.dto.request.DokumenPegawaiReq
import com.santika.simrs.module.kepegawaian.dto.response.DokumenPegawaiRes
import com.santika.simrs.module.kepegawaian.infrastructure.entities.DokumenPegawaiEntity
import com.santika.simrs.module.kepegawaian.infrastructure.jooq.DokumenPegawaiJooqRepo
import com.santika.simrs.module.kepegawaian.infrastructure.repository.DokumenPegawaiRepo
import com.santika.simrs.shared.file.temp.TempFileService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * Lifecycle dokumen pegawai dikelola lewat [PegawaiService] (create sekaligus).
 * Tidak ada CRUD publik — hanya read (GET) yang join file; akses file fisik
 * memakai controller file generik (serve by JWT / signed URL).
 */
@Service
class DokumenPegawaiService(
    private val repo: DokumenPegawaiRepo,
    private val jooqRepo: DokumenPegawaiJooqRepo,
    private val tempFileService: TempFileService,
    private val dokterService: DokterService
) {

    /**
     * Read-only: daftar dokumen milik pegawai, di-join dengan file (id + nama).
     * Bisa diakses by [pegawaiId] langsung, atau by [dokterId] (di-resolve ke pegawai-nya).
     */
    fun findAll(pegawaiId: UUID?, dokterId: UUID?): List<DokumenPegawaiRes> {
        val targetPegawaiId = pegawaiId
            ?: dokterId?.let { dokterService.getPegawaiId(it) }
            ?: throw IllegalArgumentException("Param pegawaiId atau dokterId wajib diisi")
        return jooqRepo.findAllByPegawaiId(targetPegawaiId)
    }

    /**
     * Simpan banyak dokumen sekaligus: pindahkan semua file temp → storage dalam satu
     * batch, lalu petakan tiap [DokumenPegawaiReq] ke storage id-nya via movedFromTempId.
     */
    @Transactional
    fun saveAll(pegawaiId: UUID, dokumen: List<DokumenPegawaiReq>) {
        if (dokumen.isEmpty()) return

        val tempIds = dokumen.mapNotNull { it.fileId }
        val storedByTemp = if (tempIds.isNotEmpty())
            tempFileService.moveToStorage(tempIds, "kepegawaian:dokumen", pegawaiId)
                .associateBy { it.movedFromTempId }
        else emptyMap()

        val entities = dokumen.map { req ->
            DokumenPegawaiEntity().apply {
                this.pegawaiId = pegawaiId
                namaDokumen = req.namaDokumen
                fileId = req.fileId?.let { storedByTemp[it]?.id }
                isActive = req.isActive
            }
        }
        repo.saveAll(entities)
    }
}
