package com.santika.simrs.module.master.infrastructure.repository

import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.module.master.infrastructure.entities.SupportPenjaminEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SupportPenjaminRepo : JpaRepository<SupportPenjaminEntity, UUID> {

    @Query("""
        select p from SupportPenjaminEntity p
        where (:search is null or LOWER(concat(p.penjamin, p.namaPerusahaanPenjamin)) like LOWER(concat('%', :search, '%')))
        and (:isActive is null or p.isActive = :isActive)
    """)
    fun findAllBySearch(pageable: Pageable, search: String?, isActive: Boolean?): Page<SupportPenjaminEntity>

    @Query("""
        select new com.santika.simrs.global.response.OptionsRes(p.id, CONCAT(p.penjamin, ' — ', p.namaPerusahaanPenjamin))
        from SupportPenjaminEntity p
        where p.deletedAt is null
        and (:search is null or LOWER(concat(p.penjamin, p.namaPerusahaanPenjamin)) like LOWER(concat('%', :search, '%')))
        order by p.penjamin
    """)
    fun findOptions(pageable: Pageable, search: String?): Slice<OptionsRes>
}