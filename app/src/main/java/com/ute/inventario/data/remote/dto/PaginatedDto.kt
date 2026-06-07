package com.ute.inventario.data.remote.dto

data class PaginatedResponseDto<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>,
)
