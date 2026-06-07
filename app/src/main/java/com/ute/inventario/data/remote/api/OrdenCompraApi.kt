package com.ute.inventario.data.remote.api

import com.ute.inventario.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface OrdenCompraApi {
    @GET("ordenes-compra/")
    suspend fun getOrdenes(
        @Query("page") page: Int? = null
    ): Response<PaginatedResponseDto<OrdenCompraDto>>

    @GET("ordenes-compra/{id}/")
    suspend fun getOrden(@Path("id") id: Int): Response<OrdenCompraDto>

    @POST("ordenes-compra/")
    suspend fun crearOrden(@Body body: OrdenCompraRequestDto): Response<OrdenCompraDto>

    @DELETE("ordenes-compra/{id}/")
    suspend fun eliminarOrden(@Path("id") id: Int): Response<Unit>
}
