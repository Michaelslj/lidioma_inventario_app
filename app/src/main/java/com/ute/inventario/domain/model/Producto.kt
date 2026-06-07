package com.ute.inventario.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    @SerialName("precio_con_impuesto") val precioConImpuesto: Double,
    val stock: Int,
    @SerialName("en_stock") val enStock: Boolean,
    @SerialName("es_activo") val esActivo: Boolean,
    val categoriaId: Int? = null,
    val categoriaNombre: String? = null
)
