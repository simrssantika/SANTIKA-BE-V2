package com.santika.simrs.module.kepegawaian.infrastructure.repository

import com.santika.simrs.module.kepegawaian.infrastructure.entities.DokterEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DokterRepo : JpaRepository<DokterEntity, UUID> {

    fun findAllByPegawaiId(pegawaiId: UUID): List<DokterEntity>
}
