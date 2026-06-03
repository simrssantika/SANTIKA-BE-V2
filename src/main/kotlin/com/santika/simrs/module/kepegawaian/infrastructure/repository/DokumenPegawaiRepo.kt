package com.santika.simrs.module.kepegawaian.infrastructure.repository

import com.santika.simrs.module.kepegawaian.infrastructure.entities.DokumenPegawaiEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DokumenPegawaiRepo : JpaRepository<DokumenPegawaiEntity, UUID> {

    @Query("""
        select d from DokumenPegawaiEntity d
        where d.pegawaiId = :pegawaiId
        and (:search is null or LOWER(d.namaDokumen) like LOWER(concat('%', :search, '%')))
    """)
    fun findAllByPegawaiId(pageable: Pageable, pegawaiId: UUID, search: String?): Page<DokumenPegawaiEntity>
}
