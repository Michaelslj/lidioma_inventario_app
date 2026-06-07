package com.ute.inventario.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.ute.inventario.domain.model.ChangePasswordRequest
import com.ute.inventario.domain.model.User
import com.ute.inventario.domain.model.UserProfileRequest

data class UserDto(
    val id: Int,
    val username: String,
    val email: String?,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?,
    @SerializedName("is_staff") val isStaff: Boolean?,
    @SerializedName("is_superuser") val isSuperuser: Boolean?, 
    @SerializedName("is_active") val isActive: Boolean?,
    @SerializedName("date_joined") val dateJoined: String?
)

data class UserProfileRequestDto(
    val email: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String
)

data class ChangePasswordRequestDto(
    @SerializedName("old_password") val oldPassword: String,
    @SerializedName("new_password") val newPassword: String
)

// ── Mappers ───────────────────────────────────────────────────

fun UserDto.toDomain() = User(
    id = id,
    username = username,
    email = email ?: "",
    nombreCompleto = if (!firstName.isNullOrBlank() || !lastName.isNullOrBlank()) {
        "${firstName ?: ""} ${lastName ?: ""}".trim()
    } else {
        username
    },
    // PRIORIDAD AL SUPERUSUARIO: Si el servidor dice que es Superuser o Staff, es Admin.
    isStaff = (isSuperuser == true) || (isStaff == true),
    isActive = isActive ?: true,
    fechaRegistro = dateJoined ?: ""
)

fun UserProfileRequest.toRequest() = UserProfileRequestDto(
    email = email,
    firstName = firstName,
    lastName = lastName
)

fun ChangePasswordRequest.toRequest() = ChangePasswordRequestDto(
    oldPassword = oldPassword,
    newPassword = newPassword
)
