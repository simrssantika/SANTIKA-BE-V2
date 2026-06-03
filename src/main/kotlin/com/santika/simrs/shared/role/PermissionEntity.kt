package com.santika.simrs.shared.role

import com.santika.simrs.global.entity.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction
import java.util.UUID

@Entity
@Table(name = "permissions", schema = "shared")
@SQLRestriction("deleted_at IS NULL")
class PermissionEntity : BaseEntity() {
    @Id
    @GeneratedValue
    var id: UUID? = null
    var name: String = ""
    var displayName: String = ""
}
