package com.santika.simrs.module.kepegawaian.infrastructure.entities

import com.santika.simrs.global.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "pegawai", schema = "kepegawaian")
@SQLRestriction("deleted_at is null")
class PegawaiEntity : BaseEntity() {
    @Id
    @GeneratedValue
    var id: UUID? = null
    var nama: String? = null
    var nip: String? = null
    var jenisIdentitas: String? = null
    var identitasLain: String? = null
    var noIdentitas: String? = null
    var kelamin: String? = null
    var jabatan: String? = null
    var telephone: String? = null
    var email: String? = null
    var jenjangJabatan: String? = null
    var kelompokPegawaiId: UUID? = null
    var resikoPegawaiId: UUID? = null
    var daruratPegawaiId: UUID? = null
    var departemenPegawaiId: UUID? = null
    var bidangPegawaiId: UUID? = null
    var statusPegawai: String? = "KONTRAK"
    var vendorTenagaLuarId: UUID? = null
    var pendidikan: String? = null
    var tempatLahir: String? = null
    var tanggalLahir: LocalDate? = null
    var provinsiId: UUID? = null
    var kabupatenId: UUID? = null
    var kecamatanId: UUID? = null
    var kelurahanId: UUID? = null
    var alamat: String? = null
    var mulaiBekerja: LocalDate? = null
    var fotoId: UUID? = null
    var isActive: Boolean = true
}
