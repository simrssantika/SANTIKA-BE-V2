package com.santika.simrs.module.kepegawaian.infrastructure.entities

import com.santika.simrs.global.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import java.util.UUID

@Entity
@Table(name = "vendor_tenaga_luar", schema = "kepegawaian")
@SQLRestriction("deleted_at is null")
class VendorTenagaLuarEntity : BaseEntity() {
    @Id
    @GeneratedValue
    var id: UUID? = null
    var namaVendor: String? = null
    var alamatVendor: String? = null
    var bidangVendor: String? = null
    var telephoneVendor: String? = null
    var namaKontakDarurat: String? = null
    var kontakDaruratVendor: String? = null
}
