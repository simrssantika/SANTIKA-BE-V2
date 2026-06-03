package com.santika.simrs.shared.file.storage

sealed class StorageFileEvent {

    // Dipublish setelah storage soft-deleted di DB
    // AFTER_COMMIT   → hapus file fisik dari disk
    data class Deleted(
        val path: String
    ) : StorageFileEvent()

    // Dipublish setelah storage di-rollback ke temp di DB
    // AFTER_COMMIT   → daftarkan token baru ke Redis
    data class RolledBack(
        val token: String
    ) : StorageFileEvent()
}
