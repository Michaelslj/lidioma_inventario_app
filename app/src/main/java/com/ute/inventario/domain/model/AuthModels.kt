package com.ute.inventario.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val passwordConfirm: String
)

@Serializable
data class TokenResponse(
    val access: String,
    val refresh: String,
    val userId: Int,
    val username: String,
    val isStaff: Boolean = false
)
