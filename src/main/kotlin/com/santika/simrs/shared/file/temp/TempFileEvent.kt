package com.santika.simrs.shared.file.temp

sealed class TempFileEvent {

    // Referensi satu file: token Redis + path fisik
    data class FileRef(
        val token: String,
        val path: String
    )

    // Dipublish setelah record temp disimpan ke DB (bisa banyak file sekaligus)
    // AFTER_COMMIT   → daftarkan semua token ke Redis
    // AFTER_ROLLBACK → hapus semua file fisik yang sudah terlanjur ditulis ke disk
    data class Stored(
        val files: List<FileRef>
    ) : TempFileEvent()

    // Dipublish setelah record berpindah temp → storage di DB (bisa banyak file)
    // AFTER_COMMIT   → pindahkan semua file fisik dari tempDir ke uploadDir + lepas token Redis
    // AFTER_ROLLBACK → tidak ada aksi (file tetap di tempDir, record temp tidak terhapus)
    data class MovedToStorage(
        val moves: List<Move>
    ) : TempFileEvent() {
        data class Move(
            val token: String,
            val sourcePath: String,
            val destPath: String
        )
    }

    // Dipublish setelah record temp dihapus karena dibatalkan (bisa banyak file)
    // AFTER_COMMIT   → hapus semua file fisik + lepas token Redis
    data class Cancelled(
        val files: List<FileRef>
    ) : TempFileEvent()
}
