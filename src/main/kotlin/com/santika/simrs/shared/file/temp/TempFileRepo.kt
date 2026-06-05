package com.santika.simrs.shared.file.temp

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

@Repository
interface TempFileRepo : JpaRepository<TempFileEntity, UUID> {

    fun findByUploadToken(token: String): TempFileEntity?

    fun findByUploadTokenIn(tokens: List<String>): List<TempFileEntity>

    @Query("select t from TempFileEntity t where t.expiresAt < :now")
    fun findAllExpired(now: LocalDateTime): List<TempFileEntity>
}
