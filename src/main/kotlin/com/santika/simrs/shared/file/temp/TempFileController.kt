package com.santika.simrs.shared.file.temp

import com.santika.simrs.global.response.BaseResponse
import com.santika.simrs.global.response.Response.isSuccess
import com.santika.simrs.global.security.UserContext
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Paths
import java.util.*

@RestController
@RequestMapping("/api/v1/files/temp")
class TempFileController(private val tempFileService: TempFileService) {

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(
        @RequestPart("files") files: List<MultipartFile>
    ): BaseResponse<List<TempFileDto>> {
        val userId = runCatching { UUID.fromString(UserContext.getCurrentUserId()) }.getOrNull()
        return tempFileService.store(files, userId).isSuccess("Upload berhasil", 201)
    }

    // Get file by token JWT — preview temp file sebelum di-commit ke storage
    @GetMapping("/{id}")
    fun preview(@PathVariable id: UUID): ResponseEntity<UrlResource> {
        val temp = tempFileService.findById(id)
        val resource = UrlResource(Paths.get(temp.path).toUri())
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(temp.mimeType ?: "application/octet-stream"))
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"${temp.originalName}\"")
            .body(resource)
    }
}
