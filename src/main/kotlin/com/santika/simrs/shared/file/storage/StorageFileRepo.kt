package com.santika.simrs.shared.file.storage

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface StorageFileRepo : JpaRepository<StorageFileEntity, UUID> {

    fun findByModuleAndEntityId(module: String, entityId: UUID): List<StorageFileEntity>

    // Termasuk soft-deleted — untuk rollback tracking
    @Query(
        value = "select * from shared.storage_files where id = :id",
        nativeQuery = true
    )
    fun findByIdIncludeDeleted(id: UUID): StorageFileEntity?


    fun findByIdIn(ids: List<UUID?>): List<StorageFileEntity>

}
