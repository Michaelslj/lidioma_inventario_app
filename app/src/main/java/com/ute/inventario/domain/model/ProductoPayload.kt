package com.ute.inventario.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductoPayload(
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    val esActivo: Boolean = true,
    val categoriaId: Int
)
