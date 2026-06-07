package com.santika.simrs.shared.user

import com.santika.simrs.global.annotation.uuid.UuidV7
import com.santika.simrs.global.entity.BaseEntity
import com.santika.simrs.shared.role.RoleEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction
import java.util.*

@Entity
@Table(name = "users", schema = "shared")
@SQLRestriction("deleted_at IS NULL")
class UserEntity : BaseEntity() {
    @Id
    @UuidV7
    var id: UUID? = null
    var username: String = ""
    var password: String = ""
    var email: String = ""
    var isActive: Boolean = true

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_has_role",
        schema = "shared",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: MutableList<RoleEntity> = mutableListOf()
}
