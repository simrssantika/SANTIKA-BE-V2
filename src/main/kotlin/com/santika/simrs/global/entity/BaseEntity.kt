package com.santika.simrs.global.entity

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PreUpdate
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseEntity {

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = LocalDateTime.now()

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = LocalDateTime.now()

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null

    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }

    fun softDelete() {
        deletedAt = LocalDateTime.now()
        updatedAt = LocalDateTime.now()
    }

    fun isDeleted(): Boolean = deletedAt != null

    fun restore() {
        deletedAt = null
        updatedAt = LocalDateTime.now()
    }
}
