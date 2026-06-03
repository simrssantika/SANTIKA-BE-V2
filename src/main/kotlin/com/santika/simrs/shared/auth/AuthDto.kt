package com.santika.simrs.shared.auth

import com.santika.simrs.shared.user.UserEntity

data class LoginRequestDto(
    val username: String,
    val password: String
)

data class LoginResponseDto(
    val token: String,
    val refreshToken: String,
    val profile: UserPrincipalDto
)

data class UserPrincipalDto(
    val id: String,
    val username: String,
    val email: String,
    val isActive: Boolean,
    val roles: List<String>,
    val permissions: List<String>
) {
    companion object {
        fun of(user: UserEntity) = UserPrincipalDto(
            id = user.id.toString(),
            username = user.username,
            email = user.email,
            isActive = user.isActive,
            roles = user.roles.map { it.name },
            permissions = user.roles.flatMap { it.permissions.map { p -> p.name } }.distinct()
        )
    }
}
