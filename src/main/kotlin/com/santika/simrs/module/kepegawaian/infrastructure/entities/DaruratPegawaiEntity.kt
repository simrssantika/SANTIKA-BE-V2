package com.santika.simrs.module.kepegawaian.infrastructure.entities

import com.santika.simrs.global.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import java.util.UUID

@Entity
@Table(name = "darurat_pegawai", schema = "kepegawaian")
@SQLRestriction("deleted_at is null")
class DaruratPegawaiEntity : BaseEntity() {
    @Id
    @GeneratedValue
    var id: UUID? = null
    var kodeEmergency: String? = null
    var namaEmergency: String? = null
}
