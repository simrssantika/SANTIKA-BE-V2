package com.santika.simrs.module.kepegawaian.infrastructure.entities

import com.santika.simrs.global.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import java.util.UUID

@Entity
@Table(name = "bidang_pegawai", schema = "kepegawaian")
@SQLRestriction("deleted_at is null")
class BidangPegawaiEntity : BaseEntity() {
    @Id
    @GeneratedValue
    var id: UUID? = null
    var kodeBidang: String? = null
    var namaBidang: String? = null
}
