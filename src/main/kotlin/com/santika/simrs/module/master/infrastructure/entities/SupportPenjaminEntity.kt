package com.santika.simrs.module.master.infrastructure.entities

import com.santika.simrs.global.annotation.uuid.UuidV7
import com.santika.simrs.global.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import java.util.*

@Entity
@Table(name = "support_penjamin", schema = "master")
@SQLRestriction("deleted_at is null")
class SupportPenjaminEntity : BaseEntity() {
    @Id
    @UuidV7
    var id: UUID? = null
    var penjamin: String? = null
    var namaPerusahaanPenjamin: String? = null
    var alamatPenjamin: String? = null
    var teleponPenjamin: String? = null
    var penerimaTagihan: String? = null
    var isActive: Boolean = true
}