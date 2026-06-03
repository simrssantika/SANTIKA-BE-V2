package com.santika.simrs.global.audit

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "audit", schema = "shared")
class AuditEntity(
    @Id
    @GeneratedValue
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
