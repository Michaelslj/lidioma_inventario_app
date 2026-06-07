package com.ute.inventario.data.repository

import com.ute.inventario.data.remote.api.UserApi
import com.ute.inventario.data.remote.dto.toDomain
import com.ute.inventario.data.remote.dto.toRequest
import com.ute.inventario.domain.model.ChangePasswordRequest
import com.ute.inventario.domain.model.User
import com.ute.inventario.domain.model.UserProfileRequest
import com.ute.inventario.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val api: UserApi,
) : UserRepository {

    override suspend fun getPerfil(): Result<User> = runCatching {
        val response = api.getPerfil()
        if (response.isSuccessful) {
            response.body()?.toDomain() ?: throw Exception("Usuario no encontrado")
        } else {
            throw Exception("Error ${response.code()}")
        }
    }

    override suspend fun actualizarPerfil(payload: UserProfileRequest): Result<User> = runCatching {
        val response = api.actualizarPerfil(payload.toRequest())
        if (response.isSuccessful) {
            response.body()?.toDomain() ?: throw Exception("Error al actualizar")
        } else {
            throw Exception("Error ${response.code()}")
        }
    }

    override suspend fun cambiarContrasena(payload: ChangePasswordRequest): Result<Unit> = runCatching {
        val response = api.cambiarContrasena(payload.toRequest())
        if (!response.isSuccessful) {
            throw Exception("Error ${response.code()}")
        }
    }

    override suspend fun getUsuarios(): Result<List<User>> = runCatching {
        val response = api.getUsuarios()
        if (response.isSuccessful) {
            response.body()?.map { it.toDomain() } ?: throw Exception("Respuesta vacía")
        } else {
            throw Exception("Error ${response.code()}")
        }
    }
}
