package com.santika.simrs.module.kepegawaian.infrastructure.entities

import com.santika.simrs.global.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import java.util.UUID

@Entity
@Table(name = "spesialis", schema = "kepegawaian")
@SQLRestriction("deleted_at is null")
class SpesialisEntity : BaseEntity() {
    @Id
    @GeneratedValue
    var id: UUID? = null
    var kodeSpesialis: String? = null
    var namaSpesialis: String? = null
}
