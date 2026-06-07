package com.santika.simrs.module.kepegawaian.infrastructure.entities

import com.santika.simrs.global.annotation.uuid.UuidV7
import com.santika.simrs.global.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import java.util.*

@Entity
@Table(name = "departemen_pegawai", schema = "kepegawaian")
@SQLRestriction("deleted_at is null")
class DepartemenPegawaiEntity : BaseEntity() {
    @Id
    @UuidV7
    var id: UUID? = null
    var kodeDepartemen: String? = null
    var namaDepartemen: String? = null
}
