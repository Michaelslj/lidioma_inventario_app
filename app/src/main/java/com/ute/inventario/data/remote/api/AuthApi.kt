package com.ute.inventario.data.remote.api

import com.ute.inventario.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login/")
    suspend fun login(@Body body: LoginRequestDto): Response<TokenResponseDto>

    @POST("auth/register/")
    suspend fun register(@Body body: RegisterRequestDto): Response<TokenResponseDto>

    @POST("auth/token/refresh/")
    suspend fun refreshToken(@Body body: TokenRefreshRequestDto): Response<TokenRefreshResponseDto>

    @POST("auth/logout/")
    suspend fun logout(@Body body: TokenRefreshRequestDto): Response<Unit>
}
