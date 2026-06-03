package com.santika.simrs.shared.file.temp

sealed class TempFileEvent {

    // Dipublish setelah record temp disimpan ke DB
    // AFTER_COMMIT   → daftarkan token ke Redis
    // AFTER_ROLLBACK → hapus file fisik yang sudah terlanjur ditulis ke disk
    data class Stored(
        val token: String,
        val path: String
    ) : TempFileEvent()

    // Dipublish setelah record berpindah temp → storage di DB
    // AFTER_COMMIT   → pindahkan file fisik dari tempDir ke uploadDir + lepas token Redis
    // AFTER_ROLLBACK → tidak ada aksi (file tetap di tempDir, record temp tidak terhapus)
    data class MovedToStorage(
        val token: String,
        val sourcePath: String,
        val destPath: String
    ) : TempFileEvent()

    // Dipublish setelah record temp dihapus karena dibatalkan
    // AFTER_COMMIT   → hapus file fisik + lepas token Redis
    data class Cancelled(
        val token: String,
        val path: String
    ) : TempFileEvent()
}
