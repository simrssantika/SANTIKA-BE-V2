package com.santika.simrs.module.kepegawaian.infrastructure.entities

import com.santika.simrs.global.annotation.uuid.UuidV7
import com.santika.simrs.global.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import java.util.*

@Entity
@Table(name = "dokter", schema = "kepegawaian")
@SQLRestriction("deleted_at is null")
class DokterEntity : BaseEntity() {
    @Id
    @UuidV7
    var id: UUID? = null
    var pegawaiId: UUID? = null
    var namaDokter: String? = null
    var nip: String? = null
    var telephoneDokter: String? = null
    var emailDokter: String? = null
    var spesialisId: UUID? = null
    var alumni: String? = null
    var noIjinPraktek: String? = null
    var isActive: Boolean = true
}
