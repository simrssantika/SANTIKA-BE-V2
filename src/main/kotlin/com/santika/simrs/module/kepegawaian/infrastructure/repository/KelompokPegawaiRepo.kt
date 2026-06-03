package com.santika.simrs.module.kepegawaian.infrastructure.repository

import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.module.kepegawaian.infrastructure.entities.KelompokPegawaiEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface KelompokPegawaiRepo : JpaRepository<KelompokPegawaiEntity, UUID> {

    @Query("""
        select k from KelompokPegawaiEntity k
        where (:search is null or LOWER(concat(k.kodeKelompok, k.namaKelompok)) like LOWER(concat('%', :search, '%')))
    """)
    fun findAllBySearch(pageable: Pageable, search: String?): Page<KelompokPegawaiEntity>

    @Query("""
        select new com.santika.simrs.global.response.OptionsRes(k.id, CONCAT(k.kodeKelompok, ' — ', k.namaKelompok))
        from KelompokPegawaiEntity k
        where k.deletedAt is null
        and (:search is null or LOWER(concat(k.kodeKelompok, k.namaKelompok)) like LOWER(concat('%', :search, '%')))
        order by k.namaKelompok
    """)
    fun findOptions(pageable: Pageable, search: String?): Slice<OptionsRes>
}
