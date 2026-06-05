package com.santika.simrs.module.kepegawaian.dto.request

import java.time.LocalDate
import java.util.*

data class PegawaiReq(
    val nama: String,
    val nip: String?,
    val jenisIdentitas: String?,
    val identitasLain: String?,
    val noIdentitas: String?,
    val kelamin: String?,
    val jabatan: String?,
    val telephone: String?,
    val email: String?,
    val jenjangJabatan: String?,
    val kelompokPegawaiId: UUID?,
    val resikoPegawaiId: UUID?,
    val daruratPegawaiId: UUID?,
    val departemenPegawaiId: UUID?,
    val bidangPegawaiId: UUID?,
    val statusPegawai: String?,
    val vendorTenagaLuarId: UUID?,
    val pendidikan: String?,
    val tempatLahir: String?,
    val tanggalLahir: LocalDate?,
    val provinsiId: UUID?,
    val kabupatenId: UUID?,
    val kecamatanId: UUID?,
    val kelurahanId: UUID?,
    val alamat: String?,
    val mulaiBekerja: LocalDate?,
    val isDokter: Boolean = false,
    val fotoId: UUID?,
    val dokumen: List<DokumenPegawaiReq>? = null,
    val dokter: DokterReq? = null,
    val isActive: Boolean = true
)
