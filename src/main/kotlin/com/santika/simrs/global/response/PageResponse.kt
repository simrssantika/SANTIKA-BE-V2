package com.santika.simrs.global.response

import org.springframework.data.domain.Page


data class Pages(
    var size: Int,
    var number: Int,
    var totalElements: Int,
    var totalPages: Int
)


data class PageResponse<T>(
    var content: List<T>,
    var page: Pages,
) {
    companion object {
        fun <T : Any> of(page: Page<T>) = PageResponse(
            content = page.content,
            page = Pages(
                size = page.size,
                number = page.number,
                totalElements = page.totalElements.toInt(),
                totalPages = page.totalPages
            )
        )
    }
}

