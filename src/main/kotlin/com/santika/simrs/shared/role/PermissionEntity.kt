package com.santika.simrs.shared.role

import com.santika.simrs.global.annotation.uuid.UuidV7
import com.santika.simrs.global.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import java.util.*

@Entity
@Table(name = "permissions", schema = "shared")
@SQLRestriction("deleted_at IS NULL")
class PermissionEntity : BaseEntity() {
    @Id
    @UuidV7
    var id: UUID? = null
    var name: String = ""
    var displayName: String = ""
}
