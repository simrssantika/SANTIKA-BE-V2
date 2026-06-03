package com.santika.simrs.global.cache

import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Component

@Component
class CacheProvider {

    @CacheEvict(value = ["userAuthorities"], key = "#username")
    fun clearUserAuthoritiesCache(username: String) {}

    @CacheEvict(value = ["userPrincipalDetail"], key = "#username")
    fun clearUserPrincipalCache(username: String) {}

    @CacheEvict(value = ["userAuthorities"], allEntries = true)
    fun clearAllUserAuthoritiesCache() {}

    @CacheEvict(value = ["userPrincipalDetail"], allEntries = true)
    fun clearAllUserPrincipalCache() {}
}
