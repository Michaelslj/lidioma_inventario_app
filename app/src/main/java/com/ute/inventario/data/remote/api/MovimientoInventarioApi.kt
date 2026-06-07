package com.ute.inventario.data.remote.api

import com.ute.inventario.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface MovimientoInventarioApi {
    @GET("movimientos/")
    suspend fun getMovimientos(
        @Query("page") page: Int? = null,
        @Query("producto") productoId: Int? = null,
    ): Response<PaginatedResponseDto<MovimientoInventarioDto>>

    @GET("movimientos/{id}/")
    suspend fun getMovimiento(@Path("id") id: Int): Response<MovimientoInventarioDto>

    @POST("movimientos/")
    suspend fun crearMovimiento(@Body body: MovimientoInventarioCreateDto): Response<MovimientoInventarioDto>

    @DELETE("movimientos/{id}/")
    suspend fun eliminarMovimiento(@Path("id") id: Int): Response<Unit>
}
