package com.ute.inventario.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Categoria(
    val id: Int,
    val nombre: String,
    val slug: String,
    val descripcion: String,
    val activa: Boolean,
    @SerialName("total_productos") val totalProductos: Int,
    @SerialName("creado_en") val creadoEn: String
)
