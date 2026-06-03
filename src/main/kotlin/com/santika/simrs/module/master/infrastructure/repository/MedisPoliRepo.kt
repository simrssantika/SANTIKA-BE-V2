package com.santika.simrs.module.master.infrastructure.repository

import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.module.master.infrastructure.entities.MedisPoliEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MedisPoliRepo : JpaRepository<MedisPoliEntity, UUID> {

    @Query("""
        select p from MedisPoliEntity p
        where (:search is null or LOWER(concat(p.kodePoli, p.namaPoli)) like LOWER(concat('%', :search, '%')))
    """)
    fun findAllBySearch(pageable: Pageable, search: String?): Page<MedisPoliEntity>

    @Query("""
        select new com.santika.simrs.global.response.OptionsRes(p.id, CONCAT(p.kodePoli, ' — ', p.namaPoli))
        from MedisPoliEntity p
        where p.deletedAt is null
        and (:search is null or LOWER(concat(p.kodePoli, p.namaPoli)) like LOWER(concat('%', :search, '%')))
        order by p.namaPoli
    """)
    fun findOptions(pageable: Pageable, search: String?): Slice<OptionsRes>
}