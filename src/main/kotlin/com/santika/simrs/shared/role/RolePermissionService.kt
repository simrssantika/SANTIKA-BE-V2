package com.santika.simrs.shared.role

import com.santika.simrs.global.exception.DataNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class RolePermissionService(private val roleRepository: RoleRepository) {

    fun findById(id: UUID): RoleEntity =
        roleRepository.findById(id).orElseThrow { DataNotFoundException("Role tidak ditemukan") }

    @Transactional
    fun syncUserRole(user: com.santika.simrs.shared.user.UserEntity, roleId: UUID) {
        val role = findById(roleId)
        user.roles.clear()
        user.roles.add(role)
    }
}
