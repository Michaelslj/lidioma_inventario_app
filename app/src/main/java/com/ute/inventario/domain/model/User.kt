package com.ute.inventario.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val username: String,
    val email: String,
    @SerialName("nombre_completo") val nombreCompleto: String,
    @SerialName("is_staff") val isStaff: Boolean,
    @SerialName("is_active") val isActive: Boolean,
    @SerialName("fecha_registro") val fechaRegistro: String
)

@Serializable
data class UserProfileRequest(
    val email: String,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String
)

@Serializable
data class ChangePasswordRequest(
    @SerialName("old_password") val oldPassword: String,
    @SerialName("new_password") val newPassword: String
)
