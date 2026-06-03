package com.santika.simrs.shared.user

import java.util.*

data class UserResponseDto(
    val id: String,
    val username: String,
    val email: String,
    val isActive: Boolean,
    val role: String? = null,
    val roleId: UUID? = null
) {
    companion object {
        fun of(user: UserEntity) = UserResponseDto(
            id = user.id.toString(),
            username = user.username,
            email = user.email,
            isActive = user.isActive,
            role = user.roles.firstOrNull()?.displayName,
            roleId = user.roles.firstOrNull()?.id
        )
    }
}

data class UserRequestDto(
    val username: String,
    val email: String,
    val password: String,
    val isActive: Boolean,
    val role: UUID
) {
    companion object {
        fun toEntity(dto: UserRequestDto, encodedPassword: String) = UserEntity().apply {
            username = dto.username
            email = dto.email
            isActive = dto.isActive
            password = encodedPassword
        }

        fun toUpdate(dto: UserRequestDto, encodedPassword: String, user: UserEntity) = user.also {
            it.username = dto.username
            it.email = dto.email
            it.isActive = dto.isActive
            it.password = encodedPassword
        }
    }
}
