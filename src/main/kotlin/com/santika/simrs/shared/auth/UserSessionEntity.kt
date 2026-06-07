package com.santika.simrs.shared.auth

import com.santika.simrs.global.annotation.uuid.UuidV7
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

/**
 * Satu baris = satu sesi login aktif sebuah device.
 * [id] surrogate stabil; [jti] adalah id access-token yang sedang aktif dan
 * AKAN dirotasi tiap refresh (karena itu jti dipisah dari PK).
 * Single-session: login baru me-revoke semua baris aktif milik user yang sama.
 */
@Entity
@Table(name = "user_sessions", schema = "shared")
class UserSessionEntity {
    @Id
    @UuidV7
    var id: UUID? = null

    @Column(name = "user_id", nullable = false)
    var userId: UUID? = null

    @Column(name = "jti", nullable = false)
    var jti: UUID? = null

    @Column(name = "device_info")
    var deviceInfo: String? = null

    @Column(name = "ip_address")
    var ipAddress: String? = null

    @Column(name = "user_agent")
    var userAgent: String? = null

    @Column(name = "revoked_at")
    var revokedAt: LocalDateTime? = null

    @Column(name = "revoked_reason")
    var revokedReason: String? = null

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "last_used_at")
    var lastUsedAt: LocalDateTime? = null

    @Column(name = "expires_at", nullable = false)
    var expiresAt: LocalDateTime? = null
}
