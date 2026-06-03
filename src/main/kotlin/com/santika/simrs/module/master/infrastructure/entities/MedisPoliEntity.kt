package com.santika.simrs.module.master.infrastructure.entities

import com.santika.simrs.global.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import java.util.UUID

@Entity
@Table(name = "medis_poli", schema = "master")
@SQLRestriction("deleted_at is null")
class MedisPoliEntity : BaseEntity() {
    @Id
    @GeneratedValue
    var id: UUID? = null
    var kodePoli: String? = null
    var namaPoli: String? = null
}