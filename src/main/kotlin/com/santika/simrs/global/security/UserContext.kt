package com.santika.simrs.global.security

import com.santika.simrs.global.exception.UnauthorizedException
import com.santika.simrs.shared.auth.UserPrincipalDto
import org.springframework.security.core.context.SecurityContextHolder

object UserContext {

    fun getPrincipal(): UserPrincipalDto {
        val auth = SecurityContextHolder.getContext().authentication
        return auth?.principal as? UserPrincipalDto
            ?: throw UnauthorizedException("User tidak terautentikasi")
    }

    fun getCurrentUserId(): String = getPrincipal().id

    fun getCurrentUsername(): String = getPrincipal().username

    fun hasPermission(permission: String): Boolean =
        getPrincipal().permissions.contains(permission)

    fun hasAnyPermission(vararg permissions: String): Boolean =
        permissions.any { getPrincipal().permissions.contains(it) }

    fun hasAllPermissions(vararg permissions: String): Boolean =
        permissions.all { getPrincipal().permissions.contains(it) }

    fun hasRole(role: String): Boolean =
        getPrincipal().roles.contains(role)

    fun hasAnyRole(vararg roles: String): Boolean =
        roles.any { getPrincipal().roles.contains(it) }

    fun requirePermission(permission: String) {
        if (!hasPermission(permission))
            throw UnauthorizedException("Akses ditolak: permission '$permission' diperlukan")
    }

    fun requireAnyPermission(vararg permissions: String) {
        if (!hasAnyPermission(*permissions))
            throw UnauthorizedException("Akses ditolak: salah satu permission ${permissions.toList()} diperlukan")
    }

    fun requireRole(role: String) {
        if (!hasRole(role))
            throw UnauthorizedException("Akses ditolak: role '$role*' diperlukan")
    }
}
