package com.ute.inventario.data.remote.api

import com.ute.inventario.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    // Ruta confirmada por los logs del servidor
    @GET("users/profile/")
    suspend fun getPerfil(): Response<UserDto>

    @PATCH("users/profile/")
    suspend fun actualizarPerfil(@Body body: UserProfileRequestDto): Response<UserDto>

    @POST("users/change-password/")
    suspend fun cambiarContrasena(@Body body: ChangePasswordRequestDto): Response<Unit>

    @GET("users/")
    suspend fun getUsuarios(): Response<List<UserDto>>
}
