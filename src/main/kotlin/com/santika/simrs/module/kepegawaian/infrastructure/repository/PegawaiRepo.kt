package com.santika.simrs.module.kepegawaian.infrastructure.repository

import com.santika.simrs.module.kepegawaian.infrastructure.entities.PegawaiEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PegawaiRepo : JpaRepository<PegawaiEntity, UUID>
