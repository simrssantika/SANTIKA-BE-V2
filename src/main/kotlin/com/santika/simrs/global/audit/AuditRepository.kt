package com.santika.simrs.global.audit

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AuditRepository : JpaRepository<AuditEntity, UUID>
