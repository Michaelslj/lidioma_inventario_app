package com.ute.inventario.data.remote.api

import com.ute.inventario.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ProductoApi {
    @GET("productos/")
    suspend fun getProductos(
        @QueryMap filtros: Map<String, String>,
    ): Response<PaginatedResponseDto<ProductDto>>

    @GET("productos/{id}/")
    suspend fun getProducto(@Path("id") id: Int): Response<ProductDto>

    @POST("productos/")
    suspend fun crearProducto(@Body body: ProductRequestDto): Response<ProductDto>

    @PATCH("productos/{id}/")
    suspend fun actualizarProducto(
        @Path("id") id: Int,
        @Body body: ProductRequestDto,
    ): Response<ProductDto>

    @DELETE("productos/{id}/")
    suspend fun eliminarProducto(@Path("id") id: Int): Response<Unit>
}