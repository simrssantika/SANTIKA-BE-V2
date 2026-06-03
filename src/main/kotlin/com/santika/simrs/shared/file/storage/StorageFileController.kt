package com.santika.simrs.shared.file.storage

import com.santika.simrs.global.response.BaseResponse
import com.santika.simrs.global.response.Response.isSuccess
import com.santika.simrs.shared.file.temp.TempFileDto
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.nio.file.Paths
import java.util.UUID

@RestController
@RequestMapping("/api/v1/files/storage")
class StorageFileController(private val storageFileService: StorageFileService) {

    @GetMapping("/{id}")
    fun serve(@PathVariable id: UUID): ResponseEntity<UrlResource> {
        val storage = storageFileService.findById(id)
        val resource = UrlResource(Paths.get(storage.path).toUri())
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(storage.mimeType ?: "application/octet-stream"))
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"${storage.originalName}\"")
            .body(resource)
    }

    @PostMapping("/{id}/rollback")
    fun rollback(@PathVariable id: UUID): BaseResponse<TempFileDto> =
        storageFileService.rollbackToTemp(id).isSuccess("File dikembalikan ke temp")

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): BaseResponse<Nothing?> {
        storageFileService.delete(id)
        return null.isSuccess("File dihapus")
    }
}
