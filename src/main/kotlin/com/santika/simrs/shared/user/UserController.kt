package com.santika.simrs.shared.user

import com.santika.simrs.global.response.BaseResponse
import com.santika.simrs.global.response.PageResponse
import com.santika.simrs.global.response.Response.isSuccess
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users")
class UserController(private val service: UserService) {

    @GetMapping
    fun findAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?
    ): BaseResponse<PageResponse<UserResponseDto>> {
        val result = service.findAll(PageRequest.of(page, size), search)
        return PageResponse.of(result).isSuccess("OK")
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): BaseResponse<UserResponseDto> =
        service.findById(id).isSuccess("OK")

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: UserRequestDto): BaseResponse<UserResponseDto> {
        val result = service.createUser(request)
        return result.isSuccess("User berhasil dibuat", 201)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @RequestBody request: UserRequestDto): BaseResponse<UserResponseDto> {
        val result = service.updateUser(id, request)
        return result.isSuccess("User berhasil diupdate")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): BaseResponse<Nothing?> {
        service.deleteUser(id)
        return null.isSuccess("User berhasil dihapus")
    }
}
