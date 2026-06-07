package com.santika.simrs.module.master.infrastructure.entities

import com.santika.simrs.global.annotation.uuid.UuidV7
import com.santika.simrs.global.entity.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction
import java.util.*

@Entity
@Table(name = "wilayah_kab_kota", schema = "master")
@SQLRestriction("deleted_at is null")
class WilayahKabKotaEntity : BaseEntity() {
    @Id
    @UuidV7
    var id: UUID? = null
    var provinsiId: UUID? = null
    var kodeKabKota: String? = null
    var namaKabKota: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provinsiId", insertable = false, updatable = false)
    var provinsi: WilayahProvinsiEntity? = null

    @OneToMany(mappedBy = "kabKota")
    var kecamatan: MutableList<WilayahKecamatanEntity> = mutableListOf()
}
