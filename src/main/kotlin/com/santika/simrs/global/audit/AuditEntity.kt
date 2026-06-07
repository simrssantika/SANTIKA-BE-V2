package com.santika.simrs.global.audit

import com.santika.simrs.global.annotation.uuid.UuidV7
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "audit", schema = "shared")
class AuditEntity(
    @Id
    @UuidV7
    var id: UUID? = null,

    @Column(name = "username", nullable = false)
    var username: String,

    @Column(name = "action", nullable = false)
    var action: String,

    @Column(name = "payload", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    var payload: String,

    @CreationTimestamp
    @Column(name = "timestamp", nullable = false, updatable = false)
    var timestamp: LocalDateTime? = null
)
