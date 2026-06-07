package com.santika.simrs.module.master.infrastructure.entities

import com.santika.simrs.global.annotation.uuid.UuidV7
import com.santika.simrs.global.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import java.util.*

@Entity
@Table(name = "wilayah_kelurahan", schema = "master")
@SQLRestriction("deleted_at is null")
class WilayahKelurahanEntity : BaseEntity() {
    @Id
    @UuidV7
    var id: UUID? = null
    var kecamatanId: UUID? = null
    var kodeKelurahan: String? = null
    var namaKelurahan: String? = null
}
