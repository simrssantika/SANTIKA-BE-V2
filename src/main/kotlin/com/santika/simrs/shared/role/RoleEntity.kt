package com.santika.simrs.shared.role

import com.santika.simrs.global.annotation.uuid.UuidV7
import com.santika.simrs.global.entity.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction
import java.util.*

@Entity
@Table(name = "roles", schema = "shared")
@SQLRestriction("deleted_at IS NULL")
class RoleEntity : BaseEntity() {
    @Id
    @UuidV7
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
