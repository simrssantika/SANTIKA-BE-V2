package com.santika.simrs.shared.file.temp

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "temp_files", schema = "shared")
class TempFileEntity {

    @Id
    @GeneratedValue
    var id: UUID? = null

    @Column(name = "original_name")
    var originalName: String? = null

    @Column(name = "stored_name", nullable = false)
    var storedName: String = ""

    @Column(name = "path", nullable = false)
    var path: String = ""

    @Column(name = "ext")
    var ext: String? = null

    @Column(name = "size")
    var size: Long? = null

    @Column(name = "mime_type")
    var mimeType: String? = null

    @Column(name = "upload_token", unique = true)
    var uploadToken: String? = null

    @Column(name = "uploaded_by")
    var uploadedBy: UUID? = null

    @Column(name = "expires_at", nullable = false)
    var expiresAt: LocalDateTime = LocalDateTime.now().plusHours(24)

    @Column(name = "created_at", updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()

    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }
}
