package com.santika.simrs.module.kepegawaian.infrastructure.repository

import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.module.kepegawaian.infrastructure.entities.BidangPegawaiEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BidangPegawaiRepo : JpaRepository<BidangPegawaiEntity, UUID> {

    @Query("""
        select b from BidangPegawaiEntity b
        where (:search is null or LOWER(concat(b.kodeBidang, b.namaBidang)) like LOWER(concat('%', :search, '%')))
    """)
    fun findAllBySearch(pageable: Pageable, search: String?): Page<BidangPegawaiEntity>

    @Query("""
        select new com.santika.simrs.global.response.OptionsRes(b.id, CONCAT(b.kodeBidang, ' — ', b.namaBidang))
        from BidangPegawaiEntity b
        where b.deletedAt is null
        and (:search is null or LOWER(concat(b.kodeBidang, b.namaBidang)) like LOWER(concat('%', :search, '%')))
        order by b.namaBidang
    """)
    fun findOptions(pageable: Pageable, search: String?): Slice<OptionsRes>
}
