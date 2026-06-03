package com.santika.simrs.module.master.infrastructure.repository

import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.module.master.infrastructure.entities.MedisProsedurEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MedisProsedurRepo : JpaRepository<MedisProsedurEntity, UUID> {

    @Query("""
        select p from MedisProsedurEntity p
        where (:search is null or LOWER(concat(p.kodeProsedur, p.namaProsedur)) like LOWER(concat('%', :search, '%')))
    """)
    fun findAllBySearch(pageable: Pageable, search: String?): Page<MedisProsedurEntity>

    @Query("""
        select new com.santika.simrs.global.response.OptionsRes(p.id, CONCAT(p.kodeProsedur, ' — ', p.namaProsedur))
        from MedisProsedurEntity p
        where p.deletedAt is null
        and (:search is null or LOWER(concat(p.kodeProsedur, p.namaProsedur)) like LOWER(concat('%', :search, '%')))
        order by p.namaProsedur
    """)
    fun findOptions(pageable: Pageable, search: String?): Slice<OptionsRes>
}