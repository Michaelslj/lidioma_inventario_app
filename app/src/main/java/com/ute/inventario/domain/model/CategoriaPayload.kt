package com.ute.inventario.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoriaPayload(
    val nombre: String,
    val descripcion: String,
    val activa: Boolean = true
)
