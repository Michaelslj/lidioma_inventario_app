package com.ute.inventario.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovimientoInventario(
    val id: Int,
    val producto: Int,
    @SerialName("producto_nombre") val productoNombre: String,
    @SerialName("producto_categoria") val productoCategoria: String,
    val proveedor: Int?,
    @SerialName("proveedor_nombre") val proveedorNombre: String?,
    val tipo: String,
    @SerialName("tipo_display") val tipoDisplay: String,
    val cantidad: Int,
    val motivo: String,
    val usuario: String,
    @SerialName("creado_en") val creadoEn: String
)

@Serializable
data class MovimientoInventarioPayload(
    val producto: Int,
    val proveedor: Int? = null,
    val tipo: String,
    val cantidad: Int,
    val motivo: String
)
