package com.santika.simrs.shared.role

import com.santika.simrs.global.entity.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction
import java.util.UUID

@Entity
@Table(name = "roles", schema = "shared")
@SQLRestriction("deleted_at IS NULL")
class RoleEntity : BaseEntity() {
    @Id
    @GeneratedValue
    var id: UUID? = null
    var name: String = ""
    var displayName: String = ""

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "role_has_permission",
        schema = "shared",
        joinColumns = [JoinColumn(name = "role_id")],
        inverseJoinColumns = [JoinColumn(name = "permission_id")]
    )
    var permissions: MutableList<PermissionEntity> = mutableListOf()
}
