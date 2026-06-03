package com.santika.simrs.module.master.infrastructure.entities

import com.santika.simrs.global.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import java.util.*

@Entity
@Table(name = "medis_prosedur_icd9", schema = "master")
@SQLRestriction("deleted_at is null")
class MedisProsedurEntity : BaseEntity() {
    @Id
    @GeneratedValue
    var id: UUID? = null
    var kodeProsedur: String? = null
    var namaProsedur: String? = null
}