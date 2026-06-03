package com.santika.simrs.module.kepegawaian.infrastructure.repository

import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.module.kepegawaian.infrastructure.entities.SpesialisEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SpesialisRepo : JpaRepository<SpesialisEntity, UUID> {

    @Query("""
        select s from SpesialisEntity s
        where (:search is null or LOWER(concat(s.kodeSpesialis, s.namaSpesialis)) like LOWER(concat('%', :search, '%')))
    """)
    fun findAllBySearch(pageable: Pageable, search: String?): Page<SpesialisEntity>

    @Query("""
        select new com.santika.simrs.global.response.OptionsRes(s.id, CONCAT(s.kodeSpesialis, ' — ', s.namaSpesialis))
        from SpesialisEntity s
        where s.deletedAt is null
        and (:search is null or LOWER(concat(s.kodeSpesialis, s.namaSpesialis)) like LOWER(concat('%', :search, '%')))
        order by s.namaSpesialis
    """)
    fun findOptions(pageable: Pageable, search: String?): Slice<OptionsRes>
}
