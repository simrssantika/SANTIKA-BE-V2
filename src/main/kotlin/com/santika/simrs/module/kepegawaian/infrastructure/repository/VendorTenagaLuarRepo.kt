package com.santika.simrs.module.kepegawaian.infrastructure.repository

import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.module.kepegawaian.infrastructure.entities.VendorTenagaLuarEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface VendorTenagaLuarRepo : JpaRepository<VendorTenagaLuarEntity, UUID> {

    @Query("""
        select v from VendorTenagaLuarEntity v
        where (:search is null or LOWER(concat(v.namaVendor, coalesce(v.bidangVendor, ''))) like LOWER(concat('%', :search, '%')))
    """)
    fun findAllBySearch(pageable: Pageable, search: String?): Page<VendorTenagaLuarEntity>

    @Query("""
        select new com.santika.simrs.global.response.OptionsRes(v.id, v.namaVendor)
        from VendorTenagaLuarEntity v
        where v.deletedAt is null
        and (:search is null or LOWER(v.namaVendor) like LOWER(concat('%', :search, '%')))
        order by v.namaVendor
    """)
    fun findOptions(pageable: Pageable, search: String?): Slice<OptionsRes>
}
