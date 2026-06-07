package com.santika.simrs.module.master.infrastructure.entities

import com.santika.simrs.global.annotation.uuid.UuidV7
import com.santika.simrs.global.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import java.util.*

@Entity
@Table(name = "medis_diagnosa_icd10", schema = "master")
@SQLRestriction("deleted_at is null")
class MedisDiagnosaEntity : BaseEntity() {
    @Id
    @UuidV7
    var id: UUID? = null
    var kodeDiagnosa: String? = null
    var namaDiagnosa: String? = null
}
