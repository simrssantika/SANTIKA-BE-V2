package com.santika.simrs.shared.user

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface UserRepository : JpaRepository<UserEntity, UUID> {

    @EntityGraph(attributePaths = ["roles"])
    @Query("""
        select u from UserEntity u
        where (:search is null or LOWER(u.username) like LOWER(concat('%', :search, '%'))
            or LOWER(u.email) like LOWER(concat('%', :search, '%')))
    """)
    fun findAllBySearch(pageable: Pageable, search: String?): Page<UserEntity>

    @EntityGraph(attributePaths = ["roles"])
    fun findByUsername(username: String): Optional<UserEntity>

    fun findByEmail(email: String): UserEntity?
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun existsByEmailAndIdNot(email: String, id: UUID): Boolean
    fun existsByUsernameAndIdNot(username: String, id: UUID): Boolean
}
