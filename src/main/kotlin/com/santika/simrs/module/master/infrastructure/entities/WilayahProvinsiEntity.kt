package com.santika.simrs.module.master.infrastructure.entities

import com.santika.simrs.global.annotation.uuid.UuidV7
import com.santika.simrs.global.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import java.util.*


@Entity
@Table(name = "wilayah_provinsi", schema = "master")
@SQLRestriction("deleted_at is null")
class WilayahProvinsiEntity : BaseEntity() {
    @Id
    @UuidV7
    var id: UUID? = null
    var kodeProvinsi: String? = null
    var namaProvinsi: String? = null

    @OneToMany(mappedBy = "provinsi")
    var kabKota: MutableList<WilayahKabKotaEntity> = mutableListOf()
}