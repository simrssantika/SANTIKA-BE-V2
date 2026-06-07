package com.santika.simrs.module.master.infrastructure.entities

import com.santika.simrs.global.annotation.uuid.UuidV7
import com.santika.simrs.global.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import java.util.*

@Entity
@Table(name = "medis_poli", schema = "master")
@SQLRestriction("deleted_at is null")
class MedisPoliEntity : BaseEntity() {
    @Id
    @UuidV7
    var id: UUID? = null
    var kodePoli: String? = null
    var namaPoli: String? = null
}