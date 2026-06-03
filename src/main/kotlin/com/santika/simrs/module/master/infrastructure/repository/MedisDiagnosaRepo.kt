package com.santika.simrs.module.master.infrastructure.repository

import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.module.master.infrastructure.entities.MedisDiagnosaEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MedisDiagnosaRepo : JpaRepository<MedisDiagnosaEntity, UUID> {

    @Query("""
        select d from MedisDiagnosaEntity d
        where (:search is null or LOWER(concat(d.kodeDiagnosa, d.namaDiagnosa)) like LOWER(concat('%', :search, '%')))
    """)
    fun findAllBySearch(pageable: Pageable, search: String?): Page<MedisDiagnosaEntity>

    @Query("""
        select new com.santika.simrs.global.response.OptionsRes(d.id, CONCAT(d.kodeDiagnosa, ' — ', d.namaDiagnosa))
        from MedisDiagnosaEntity d
        where d.deletedAt is null
        and (:search is null or LOWER(concat(d.kodeDiagnosa, d.namaDiagnosa)) like LOWER(concat('%', :search, '%')))
        order by d.namaDiagnosa
    """)
    fun findOptions(pageable: Pageable, search: String?): Slice<OptionsRes>
}