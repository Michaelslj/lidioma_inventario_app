package com.ute.inventario.data.remote.api

import com.ute.inventario.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ProveedorApi {
    @GET("proveedores/")
    suspend fun getProveedores(
        @Query("page") page: Int? = null
    ): Response<PaginatedResponseDto<ProveedorDto>>

    @GET("proveedores/{id}/")
    suspend fun getProveedor(@Path("id") id: Int): Response<ProveedorDto>

    @POST("proveedores/")
    suspend fun crearProveedor(@Body body: ProveedorRequestDto): Response<ProveedorDto>

    @PATCH("proveedores/{id}/")
    suspend fun actualizarProveedor(
        @Path("id") id: Int,
        @Body body: ProveedorRequestDto
    ): Response<ProveedorDto>

    @DELETE("proveedores/{id}/")
    suspend fun eliminarProveedor(@Path("id") id: Int): Response<Unit>
}
