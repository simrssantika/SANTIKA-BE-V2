package com.santika.simrs.module.kepegawaian.infrastructure.repository

import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.module.kepegawaian.infrastructure.entities.DaruratPegawaiEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DaruratPegawaiRepo : JpaRepository<DaruratPegawaiEntity, UUID> {

    @Query("""
        select d from DaruratPegawaiEntity d
        where (:search is null or LOWER(concat(d.kodeEmergency, d.namaEmergency)) like LOWER(concat('%', :search, '%')))
    """)
    fun findAllBySearch(pageable: Pageable, search: String?): Page<DaruratPegawaiEntity>

    @Query("""
        select new com.santika.simrs.global.response.OptionsRes(d.id, CONCAT(d.kodeEmergency, ' — ', d.namaEmergency))
        from DaruratPegawaiEntity d
        where d.deletedAt is null
        and (:search is null or LOWER(concat(d.kodeEmergency, d.namaEmergency)) like LOWER(concat('%', :search, '%')))
        order by d.namaEmergency
    """)
    fun findOptions(pageable: Pageable, search: String?): Slice<OptionsRes>
}
