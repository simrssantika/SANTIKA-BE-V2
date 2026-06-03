package com.santika.simrs.shared.user

import com.santika.simrs.global.exception.DataNotFoundException
import com.santika.simrs.shared.role.RolePermissionService
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UserService(
    private val repo: UserRepository,
    private val rolePermissionService: RolePermissionService,
    private val passwordEncoder: PasswordEncoder
) {

    fun findAll(pageable: Pageable, search: String?) =
        repo.findAllBySearch(pageable, search).map { UserResponseDto.of(it) }

    fun findById(id: UUID): UserResponseDto =
        UserResponseDto.of(repo.findById(id).orElseThrow { DataNotFoundException("User tidak ditemukan") })

    @Transactional
    fun createUser(request: UserRequestDto): UserResponseDto {
        if (repo.existsByUsername(request.username)) throw IllegalArgumentException("Username sudah digunakan")
        if (repo.existsByEmail(request.email)) throw IllegalArgumentException("Email sudah digunakan")

        val data = repo.save(UserRequestDto.toEntity(request, passwordEncoder.encode(request.password).toString()))
        rolePermissionService.syncUserRole(data, request.role)
        return UserResponseDto.of(data)
    }

    @Transactional
    fun updateUser(id: UUID, request: UserRequestDto): UserResponseDto {
        if (repo.existsByEmailAndIdNot(request.email, id)) throw IllegalArgumentException("Email sudah digunakan")
        if (repo.existsByUsernameAndIdNot(request.username, id)) throw IllegalArgumentException("Username sudah digunakan")

        val user = repo.findById(id).orElseThrow { DataNotFoundException("User tidak ditemukan") }
        val encodedPassword = if (request.password.isBlank()) user.password
        else passwordEncoder.encode(request.password).toString()

        val data = repo.save(UserRequestDto.toUpdate(request, encodedPassword, user))
        rolePermissionService.syncUserRole(user, request.role)
        return UserResponseDto.of(data)
    }

    @Transactional
    fun deleteUser(id: UUID) {
        val data = repo.findById(id).orElseThrow { DataNotFoundException("User tidak ditemukan") }
        data.softDelete()
        repo.save(data)
    }
}
