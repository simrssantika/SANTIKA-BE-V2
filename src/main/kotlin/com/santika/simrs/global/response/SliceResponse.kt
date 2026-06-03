package com.santika.simrs.global.response

import org.springframework.data.domain.Slice

data class SliceResponse<T>(
    val content: List<T>,
    val page: SlicePage
)

data class SlicePage(
    val size: Int,
    val hasNext: Boolean,
    val hasPrevious: Boolean
)

fun <T : Any> Slice<T>.toSliceResponse() = SliceResponse(
    content = this.content,
    page = SlicePage(
        size = this.size,
        hasNext = this.hasNext(),
        hasPrevious = this.hasPrevious()
    )
)
