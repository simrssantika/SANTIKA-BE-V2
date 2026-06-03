package com.santika.simrs.module.master.infrastructure.entities

import com.santika.simrs.global.entity.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction
import java.util.*


@Entity
@Table(name = "wilayah_kecamatan", schema = "master")
@SQLRestriction("deleted_at is null")
class WilayahKecamatanEntity : BaseEntity() {

    @Id
    @GeneratedValue
    var id: UUID? = null
    var kabKotaId: UUID? = null
    var kodeKecamatan: String? = null
    var namaKecamatan: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kabKotaId", insertable = false, updatable = false)
    var kabKota: WilayahKabKotaEntity? = null
}