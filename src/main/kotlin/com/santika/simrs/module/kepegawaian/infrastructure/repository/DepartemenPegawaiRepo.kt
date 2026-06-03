package com.santika.simrs.module.kepegawaian.infrastructure.repository

import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.module.kepegawaian.infrastructure.entities.DepartemenPegawaiEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DepartemenPegawaiRepo : JpaRepository<DepartemenPegawaiEntity, UUID> {

    @Query("""
        select d from DepartemenPegawaiEntity d
        where (:search is null or LOWER(concat(d.kodeDepartemen, d.namaDepartemen)) like LOWER(concat('%', :search, '%')))
    """)
    fun findAllBySearch(pageable: Pageable, search: String?): Page<DepartemenPegawaiEntity>

    @Query("""
        select new com.santika.simrs.global.response.OptionsRes(d.id, CONCAT(d.kodeDepartemen, ' — ', d.namaDepartemen))
        from DepartemenPegawaiEntity d
        where d.deletedAt is null
        and (:search is null or LOWER(concat(d.kodeDepartemen, d.namaDepartemen)) like LOWER(concat('%', :search, '%')))
        order by d.namaDepartemen
    """)
    fun findOptions(pageable: Pageable, search: String?): Slice<OptionsRes>
}
