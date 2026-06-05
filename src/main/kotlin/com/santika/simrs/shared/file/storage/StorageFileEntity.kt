package com.santika.simrs.shared.file.storage

import com.santika.simrs.global.entity.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction
import java.util.*

@Entity
@Table(name = "storage_files", schema = "shared")
@SQLRestriction("deleted_at is null")
class StorageFileEntity : BaseEntity() {

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

    @Column(name = "module")
    var module: String? = null

    @Column(name = "entity_id")
    var entityId: UUID? = null

    @Column(name = "uploaded_by")
    var uploadedBy: UUID? = null

    // Tidak pakai FK agar temp_files bisa dihapus bebas tanpa CASCADE
    @Column(name = "moved_from_temp_id")
    var movedFromTempId: UUID? = null

    @Version
    @Column(name = "version", nullable = false)
    var version: Long = 0
}
