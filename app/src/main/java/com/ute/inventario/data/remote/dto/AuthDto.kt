package com.ute.inventario.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.ute.inventario.domain.model.LoginRequest
import com.ute.inventario.domain.model.RegisterRequest
import com.ute.inventario.domain.model.TokenResponse

data class LoginRequestDto(
    val username: String,
    val password: String
)

data class RegisterRequestDto(
    val username: String,
    val email: String,
    val password: String,
    @SerializedName("password2") val password2: String
)

data class TokenResponseDto(
    val access: String,
    val refresh: String,
    @SerializedName("user_id") val userId: Int,
    val username: String,
    val email: String,
    @SerializedName("is_staff") val isStaff: Boolean? = null,
    @SerializedName("is_superuser") val isSuperuser: Boolean? = null
)

data class TokenRefreshRequestDto(
    val refresh: String
)

data class TokenRefreshResponseDto(
    val access: String,
    val refresh: String?
)

// ── Mappers ───────────────────────────────────────────────────

fun LoginRequest.toRequest() = LoginRequestDto(
    username = username,
    password = password
)

fun RegisterRequest.toRequest() = RegisterRequestDto(
    username = username,
    email = email,
    password = password,
    password2 = passwordConfirm
)

fun TokenResponseDto.toDomain() = TokenResponse(
    access = access,
    refresh = refresh,
    userId = userId,
    username = username,
    isStaff = (isStaff == true) || (isSuperuser == true)
)
