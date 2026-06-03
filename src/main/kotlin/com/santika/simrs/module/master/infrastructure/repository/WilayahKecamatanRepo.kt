package com.santika.simrs.module.master.infrastructure.repository

import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.module.master.infrastructure.entities.WilayahKecamatanEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

import java.util.*

@Repository
interface WilayahKecamatanRepo : JpaRepository<WilayahKecamatanEntity, UUID> {

    @EntityGraph(attributePaths = ["kabKota"])
    override fun findAll(pageable: Pageable): Page<WilayahKecamatanEntity>

    @EntityGraph(attributePaths = ["kabKota"])
    @Query(
        value = """
        select w from WilayahKecamatanEntity w 
        where (w.kabKota.id = :kabKotaId)
        and (:search is null or concat(
            LOWER(w.kodeKecamatan),
            LOWER(w.namaKecamatan),
            LOWER(w.kabKota.namaKabKota)
        ) like concat('%', LOWER(:search), '%'))
    """,
        countQuery = """
        select count(w) from WilayahKecamatanEntity w 
        where w.kabKota.id = :kabKotaId 
        and (:search is null or concat(
            LOWER(w.kodeKecamatan),
            LOWER(w.namaKecamatan),
            LOWER(w.kabKota.namaKabKota)
        ) like concat('%', LOWER(:search), '%'))
    """
    )
    fun findAllByKabKotaIdAndSearch(pageable: Pageable, kabKotaId: UUID?, search: String?): Page<WilayahKecamatanEntity>

    @Query("""
        select new com.santika.simrs.global.response.OptionsRes(w.id, w.namaKecamatan)
        from WilayahKecamatanEntity w
        where w.deletedAt is null
        and (:search is null or LOWER(w.namaKecamatan) like LOWER(concat('%', :search, '%')))
        order by w.namaKecamatan
    """)
    fun findOptions(pageable: Pageable, search: String?): Slice<OptionsRes>
}
