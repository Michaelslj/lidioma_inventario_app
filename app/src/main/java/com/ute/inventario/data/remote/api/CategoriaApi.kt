package com.ute.inventario.data.remote.api

import com.ute.inventario.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface CategoriaApi {
    @GET("categorias/")
    suspend fun getCategorias(): Response<PaginatedResponseDto<CategoryDto>>

    @GET("categorias/{id}/")
    suspend fun getCategoria(@Path("id") id: Int): Response<CategoryDto>

    @POST("categorias/")
    suspend fun crearCategoria(@Body body: CategoryRequestDto): Response<CategoryDto>

    @PATCH("categorias/{id}/")
    suspend fun actualizarCategoria(
        @Path("id") id: Int,
        @Body body: CategoryRequestDto,
    ): Response<CategoryDto>

    @DELETE("categorias/{id}/")
    suspend fun eliminarCategoria(@Path("id") id: Int): Response<Unit>

    @GET("categorias/stats/")
    suspend fun getStats(): Response<CategoryStatsDto>
}