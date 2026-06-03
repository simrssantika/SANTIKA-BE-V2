package com.santika.simrs.module.master.infrastructure.repository

import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.module.master.infrastructure.entities.WilayahProvinsiEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface WilayahProvinsiRepo : JpaRepository<WilayahProvinsiEntity, UUID> {

    @Query("""
        select p from WilayahProvinsiEntity p
        where (:search is null or LOWER(concat(p.kodeProvinsi, p.namaProvinsi)) like LOWER(concat('%', :search, '%')))
    """)
    fun findAllBySearch(pageable: Pageable, search: String?): Page<WilayahProvinsiEntity>

    @Query("""
        select new com.santika.simrs.global.response.OptionsRes(p.id, p.namaProvinsi)
        from WilayahProvinsiEntity p
        where p.deletedAt is null
        and (:search is null or LOWER(p.namaProvinsi) like LOWER(concat('%', :search, '%')))
        order by p.namaProvinsi
    """)
    fun findOptions(pageable: Pageable, search: String?): Slice<OptionsRes>
}