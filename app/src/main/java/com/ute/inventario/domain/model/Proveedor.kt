package com.ute.inventario.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Proveedor(
    val id: Int,
    val nombre: String,
    val ruc: String,
    val telefono: String,
    val email: String,
    val direccion: String,
    @SerialName("es_activo") val esActivo: Boolean,
    @SerialName("creado_en") val creadoEn: String
)

@Serializable
data class ProveedorPayload(
    val nombre: String,
    val ruc: String,
    val telefono: String = "",
    val email: String = "",
    val direccion: String = "",
    val esActivo: Boolean = true
)
