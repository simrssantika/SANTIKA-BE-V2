package com.santika.simrs.module.kepegawaian.infrastructure.repository

import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.module.kepegawaian.infrastructure.entities.ResikoPegawaiEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ResikoPegawaiRepo : JpaRepository<ResikoPegawaiEntity, UUID> {

    @Query("""
        select r from ResikoPegawaiEntity r
        where (:search is null or LOWER(concat(r.kodeResiko, r.namaResiko)) like LOWER(concat('%', :search, '%')))
    """)
    fun findAllBySearch(pageable: Pageable, search: String?): Page<ResikoPegawaiEntity>

    @Query("""
        select new com.santika.simrs.global.response.OptionsRes(r.id, CONCAT(r.kodeResiko, ' — ', r.namaResiko))
        from ResikoPegawaiEntity r
        where r.deletedAt is null
        and (:search is null or LOWER(concat(r.kodeResiko, r.namaResiko)) like LOWER(concat('%', :search, '%')))
        order by r.namaResiko
    """)
    fun findOptions(pageable: Pageable, search: String?): Slice<OptionsRes>
}
