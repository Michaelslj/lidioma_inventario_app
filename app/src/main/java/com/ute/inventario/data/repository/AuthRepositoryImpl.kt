package com.ute.inventario.data.repository

import com.ute.inventario.data.local.TokenDataStore
import com.ute.inventario.data.remote.api.AuthApi
import com.ute.inventario.data.remote.dto.TokenRefreshRequestDto
import com.ute.inventario.data.remote.dto.toDomain
import com.ute.inventario.data.remote.dto.toRequest
import com.ute.inventario.domain.model.LoginRequest
import com.ute.inventario.domain.model.RegisterRequest
import com.ute.inventario.domain.model.TokenResponse
import com.ute.inventario.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val tokenDataStore: TokenDataStore,
) : AuthRepository {

    override suspend fun login(credentials: LoginRequest): Result<TokenResponse> = try {
        val response = api.login(credentials.toRequest())
        if (response.isSuccessful) {
            val authResponse = response.body() ?: throw Exception("Respuesta vacía")
            
            // Unificamos is_staff e is_superuser para determinar permisos de Admin
            val isAdmin = (authResponse.isStaff == true) || (authResponse.isSuperuser == true)
            
            tokenDataStore.saveTokens(
                authResponse.access, 
                authResponse.refresh, 
                isAdmin
            )
            Result.success(authResponse.toDomain())
        } else {
            val errorBody = response.errorBody()?.string() ?: ""
            val message = if (errorBody.contains("<!DOCTYPE html>") || errorBody.contains("<html>")) {
                "Error interno del servidor (500). Contacte al administrador."
            } else if (response.code() == 401) {
                "Usuario o contraseña incorrectos"
            } else {
                errorBody.ifBlank { "Error ${response.code()}" }
            }
            Result.failure(Exception(message))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun register(data: RegisterRequest): Result<TokenResponse> = try {
        val response = api.register(data.toRequest())
        if (response.isSuccessful) {
            val authResponse = response.body() ?: throw Exception("Respuesta vacía")
            tokenDataStore.saveTokens(authResponse.access, authResponse.refresh, false)
            Result.success(authResponse.toDomain())
        } else {
            val errorBody = response.errorBody()?.string() ?: ""
            val message = if (errorBody.contains("<!DOCTYPE html>") || errorBody.contains("<html>")) {
                "Error interno del servidor (500). Contacte al administrador."
            } else {
                errorBody.ifBlank { "Error ${response.code()}" }
            }
            Result.failure(Exception(message))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        val refresh = tokenDataStore.refreshToken.first()
        if (refresh != null) {
            api.logout(TokenRefreshRequestDto(refresh))
        }
        tokenDataStore.clearSession()
    }

    override suspend fun estaAutenticado(): Boolean {
        return tokenDataStore.accessToken.first() != null
    }
}
