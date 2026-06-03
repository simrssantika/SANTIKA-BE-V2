package com.santika.simrs.module.master.infrastructure.entities

import com.santika.simrs.global.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import java.util.*

@Entity
@Table(name = "medis_diagnosa_icd10", schema = "master")
@SQLRestriction("deleted_at is null")
class MedisDiagnosaEntity : BaseEntity() {
    @Id
    @GeneratedValue
    var id: UUID? = null
    var kodeDiagnosa: String? = null
    var namaDiagnosa: String? = null
}
