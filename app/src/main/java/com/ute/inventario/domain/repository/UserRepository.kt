package com.ute.inventario.domain.repository

import com.ute.inventario.domain.model.User
import com.ute.inventario.domain.model.UserProfileRequest
import com.ute.inventario.domain.model.ChangePasswordRequest

interface UserRepository {
    suspend fun getPerfil(): Result<User>

    suspend fun actualizarPerfil(payload: UserProfileRequest): Result<User>

    suspend fun cambiarContrasena(payload: ChangePasswordRequest): Result<Unit>

    // Opcional: Solo si el rol de usuario permite listar otros usuarios
    suspend fun getUsuarios(): Result<List<User>>
}