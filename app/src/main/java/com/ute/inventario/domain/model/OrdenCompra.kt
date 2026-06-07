package com.ute.inventario.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductoDetalle(
    val id: Int,
    val nombre: String,
    val precio: String
)

@Serializable
data class OrdenCompra(
    val id: Int,
    @SerialName("codigo_orden") val codigoOrden: String,
    val proveedor: Int,
    @SerialName("proveedor_nombre") val proveedorNombre: String,
    val usuario: String,
    val estado: String,
    @SerialName("estado_display") val estadoDisplay: String,
    @SerialName("total_estimado") val totalEstimado: Double,
    val productos: List<Int>,
    @SerialName("productos_detalles") val productosDetalles: List<ProductoDetalle>,
    @SerialName("creado_en") val creadoEn: String
)

@Serializable
data class OrdenCompraPayload(
    val codigo_orden: String,
    val proveedor: Int,
    val productos: List<Int>,
    val estado: String = "PENDIENTE"
)
