package com.santika.simrs.shared.file.signed

import com.santika.simrs.global.exception.UnauthorizedException
import com.santika.simrs.global.response.BaseResponse
import com.santika.simrs.global.response.Response.isSuccess
import com.santika.simrs.global.security.UserContext
import com.santika.simrs.global.signedurl.SignedUrl
import com.santika.simrs.global.signedurl.SignedUrlService
import com.santika.simrs.shared.file.storage.StorageFileService
import com.santika.simrs.shared.file.temp.TempFileService
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.nio.file.Paths
import java.util.UUID

/**
 * Konsumen [SignedUrlService] global untuk melayani file via signed URL.
 *
 * - generate: butuh login (mint URL) — hanya user terautentikasi yang boleh
 * - access:   publik, divalidasi oleh SignedUrlInterceptor (tanpa JWT)
 */
@RestController
@RequestMapping("/api/v1/files/signed")
class SignedFileController(
    private val signedUrlService: SignedUrlService,
    private val tempFileService: TempFileService,
    private val storageFileService: StorageFileService
) {
    companion object {
        private val VALID_TYPES = setOf("temp", "storage")
        private const val BASE = "/api/v1/files/signed"
    }

    // Mint signed URL — wajib login. UserContext.getPrincipal() melempar 401 jika tanpa token valid.
    @GetMapping("/generate/{type}/{id}")
    fun generate(
        @PathVariable type: String,
        @PathVariable id: UUID,
        @RequestParam(required = false) ttl: Long?
    ): BaseResponse<SignedUrl> {
        UserContext.getPrincipal() // enforce autentikasi
        require(type in VALID_TYPES) { "Tipe resource tidak valid: $type" }
        return signedUrlService.signUrl("$BASE/$type/$id", ttl).isSuccess("OK")
    }

    // Serve file — signature sudah divalidasi SignedUrlInterceptor sebelum sampai sini
    @GetMapping("/{type}/{id}")
    fun access(
        @PathVariable type: String,
        @PathVariable id: UUID,
        @RequestParam expires: Long,
        @RequestParam signature: String
    ): ResponseEntity<UrlResource> {
        val (path, mime, name) = when (type) {
            "temp"    -> tempFileService.findById(id).let { Triple(it.path, it.mimeType, it.originalName) }
            "storage" -> storageFileService.findById(id).let { Triple(it.path, it.mimeType, it.originalName) }
            else      -> throw UnauthorizedException("Tipe resource tidak valid")
        }

        val resource = UrlResource(Paths.get(path).toUri())
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(mime ?: "application/octet-stream"))
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"$name\"")
            .body(resource)
    }
}
