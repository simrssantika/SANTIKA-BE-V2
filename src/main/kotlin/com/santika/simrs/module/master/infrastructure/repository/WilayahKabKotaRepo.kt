package com.santika.simrs.module.master.infrastructure.repository

import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.module.master.infrastructure.entities.WilayahKabKotaEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface WilayahKabKotaRepo : JpaRepository<WilayahKabKotaEntity, UUID> {

    @EntityGraph(attributePaths = ["provinsi"])
    override fun findAll(pageable: Pageable): Page<WilayahKabKotaEntity>

    @EntityGraph(attributePaths = ["provinsi"])
    @Query(
        value = """
        select w from WilayahKabKotaEntity  w 
        where (:provinsiId is null or w.provinsi.id = :provinsiId) 
        and (:search is null or concat(
            LOWER(w.kodeKabKota),
            LOWER(w.namaKabKota),
            LOWER(w.provinsi.namaProvinsi)
        ) like concat('%', LOWER(:search), '%'))
    """,
        countQuery = """
        select count(w) from WilayahKabKotaEntity w
        where (:provinsiId is null or w.provinsi.id = :provinsiId) 
        and (:search is null or concat(
            LOWER(w.kodeKabKota),
            LOWER(w.namaKabKota),
            LOWER(w.provinsi.namaProvinsi)
        ) like concat('%', LOWER(:search), '%'))
    """
    )
    fun findAllByProvinsiIdOrSearch(
        pageable: Pageable,
        provinsiId: UUID?,
        string: String?
    ): Page<WilayahKabKotaEntity>

    @Query("""
        select new com.santika.simrs.global.response.OptionsRes(w.id, w.namaKabKota)
        from WilayahKabKotaEntity w
        where w.deletedAt is null
        and (:search is null or LOWER(w.namaKabKota) like LOWER(concat('%', :search, '%')))
        order by w.namaKabKota
    """)
    fun findOptions(pageable: Pageable, search: String?): Slice<OptionsRes>
}