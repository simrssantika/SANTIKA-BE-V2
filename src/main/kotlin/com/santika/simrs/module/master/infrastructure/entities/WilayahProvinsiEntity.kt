package com.santika.simrs.module.master.infrastructure.entities

import com.santika.simrs.global.entity.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction
import java.util.*


@Entity
@Table(name = "wilayah_provinsi", schema = "master")
@SQLRestriction("deleted_at is null")
class WilayahProvinsiEntity : BaseEntity() {
    @Id
    @GeneratedValue
    var id: UUID? = null
    var kodeProvinsi: String? = null
    var namaProvinsi: String? = null

    @OneToMany(mappedBy = "provinsi")
    var kabKota: MutableList<WilayahKabKotaEntity> = mutableListOf()
}