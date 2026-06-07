package com.ute.inventario.domain.repository

import com.ute.inventario.domain.model.LoginRequest
import com.ute.inventario.domain.model.TokenResponse
import com.ute.inventario.domain.model.RegisterRequest

interface AuthRepository {
    suspend fun login(credentials: LoginRequest): Result<TokenResponse>
    suspend fun register(data: RegisterRequest): Result<TokenResponse>
    suspend fun logout(): Result<Unit>
    suspend fun estaAutenticado(): Boolean
}