package com.santika.simrs.shared.auth

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserSessionRepository : JpaRepository<UserSessionEntity, UUID> {

    /** Semua sesi yang masih aktif (belum di-revoke) milik satu user. */
    fun findByUserIdAndRevokedAtIsNull(userId: UUID): List<UserSessionEntity>

    /** Cari sesi berdasarkan jti yang sedang aktif. */
    fun findByJti(jti: UUID): UserSessionEntity?
}
