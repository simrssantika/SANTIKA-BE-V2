package com.santika.simrs.shared.file.support

import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors

/**
 * Helper paralelisasi I/O file berbasis virtual threads (JDK 21+).
 *
 * PENTING: jangan jalankan operasi DB/JPA di dalam blok ini — EntityManager &
 * koneksi JDBC terikat ke thread transaksi (ThreadLocal). Hanya untuk operasi
 * file fisik (tulis/pindah/hapus) yang aman lintas thread.
 *
 * Pakai virtual thread per task: ringan untuk blocking I/O, executor.close()
 * menunggu semua task selesai sebelum lanjut sehingga pemanggil tetap sinkron.
 */
object FileParallel {

    /**
     * Jalankan [task] untuk tiap elemen [items] secara paralel, hasil dikembalikan
     * berurutan sesuai input. Jika ada task gagal, exception penyebabnya dilempar
     * ulang setelah seluruh task selesai (cleanup parsial jadi tanggung jawab pemanggil).
     */
    fun <T, R> map(items: List<T>, task: (T) -> R): List<R> {
        if (items.isEmpty()) return emptyList()
        Executors.newVirtualThreadPerTaskExecutor().use { executor ->
            val futures = items.map { item -> executor.submit<R> { task(item) } }
            return futures.map {
                try {
                    it.get()
                } catch (e: ExecutionException) {
                    throw e.cause ?: e
                }
            }
        }
    }

    /**
     * Jalankan [task] untuk tiap elemen [items] secara paralel tanpa hasil.
     * Best-effort: kegagalan tiap task ditangani sendiri oleh [task] (mis. runCatching).
     */
    fun <T> forEach(items: List<T>, task: (T) -> Unit) {
        if (items.isEmpty()) return
        Executors.newVirtualThreadPerTaskExecutor().use { executor ->
            items.forEach { item -> executor.submit { task(item) } }
        } // close() menunggu semua task selesai
    }
}
