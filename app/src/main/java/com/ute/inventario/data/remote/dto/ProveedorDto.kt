package com.ute.inventario.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.ute.inventario.domain.model.Proveedor
import com.ute.inventario.domain.model.ProveedorPayload

data class ProveedorDto(
    val id: Int,
    val nombre: String,
    val ruc: String,
    val telefono: String?,
    val email: String?,
    val direccion: String?,
    @SerializedName("es_activo") val esActivo: Boolean,
    @SerializedName("creado_en") val creadoEn: String
)

data class ProveedorRequestDto(
    val nombre: String,
    val ruc: String,
    val telefono: String,
    val email: String,
    val direccion: String,
    @SerializedName("es_activo") val esActivo: Boolean
)

// ── Mappers ───────────────────────────────────────────────────

fun ProveedorDto.toDomain() = Proveedor(
    id = id,
    nombre = nombre,
    ruc = ruc,
    telefono = telefono ?: "",
    email = email ?: "",
    direccion = direccion ?: "",
    esActivo = esActivo,
    creadoEn = creadoEn
)

fun ProveedorPayload.toRequest() = ProveedorRequestDto(
    nombre = nombre,
    ruc = ruc,
    telefono = telefono,
    email = email,
    direccion = direccion,
    esActivo = esActivo
)
