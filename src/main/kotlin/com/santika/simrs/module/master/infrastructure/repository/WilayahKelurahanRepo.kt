package com.santika.simrs.module.master.infrastructure.repository

import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.module.master.infrastructure.entities.WilayahKelurahanEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface WilayahKelurahanRepo : JpaRepository<WilayahKelurahanEntity, UUID> {

    @Query("""
        select k from WilayahKelurahanEntity k
        where (:kecamatanId is null or k.kecamatanId = :kecamatanId)
        and (:search is null or LOWER(concat(k.kodeKelurahan, k.namaKelurahan)) like LOWER(concat('%', :search, '%')))
    """)
    fun findAllByKecamatanIdAndSearch(pageable: Pageable, kecamatanId: UUID?, search: String?): Page<WilayahKelurahanEntity>

    @Query("""
        select new com.santika.simrs.global.response.OptionsRes(k.id, k.namaKelurahan)
        from WilayahKelurahanEntity k
        where k.deletedAt is null
        and (:search is null or LOWER(k.namaKelurahan) like LOWER(concat('%', :search, '%')))
        order by k.namaKelurahan
    """)
    fun findOptions(pageable: Pageable, search: String?): Slice<OptionsRes>
}