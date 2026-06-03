package com.santika.simrs.module.kepegawaian.dto.response

import com.santika.simrs.module.kepegawaian.infrastructure.entities.PegawaiEntity
import java.time.LocalDate
import java.util.UUID

data class PegawaiRes(
    val id: UUID?,
    val nama: String?,
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
    val fotoId: UUID?,
    val isActive: Boolean
) {
    companion object {
        fun of(entity: PegawaiEntity) = PegawaiRes(
            id = entity.id,
            nama = entity.nama,
            nip = entity.nip,
            jenisIdentitas = entity.jenisIdentitas,
            identitasLain = entity.identitasLain,
            noIdentitas = entity.noIdentitas,
            kelamin = entity.kelamin,
            jabatan = entity.jabatan,
            telephone = entity.telephone,
            email = entity.email,
            jenjangJabatan = entity.jenjangJabatan,
            kelompokPegawaiId = entity.kelompokPegawaiId,
            resikoPegawaiId = entity.resikoPegawaiId,
            daruratPegawaiId = entity.daruratPegawaiId,
            departemenPegawaiId = entity.departemenPegawaiId,
            bidangPegawaiId = entity.bidangPegawaiId,
            statusPegawai = entity.statusPegawai,
            vendorTenagaLuarId = entity.vendorTenagaLuarId,
            pendidikan = entity.pendidikan,
            tempatLahir = entity.tempatLahir,
            tanggalLahir = entity.tanggalLahir,
            provinsiId = entity.provinsiId,
            kabupatenId = entity.kabupatenId,
            kecamatanId = entity.kecamatanId,
            kelurahanId = entity.kelurahanId,
            alamat = entity.alamat,
            mulaiBekerja = entity.mulaiBekerja,
            fotoId = entity.fotoId,
            isActive = entity.isActive
        )
    }
}
