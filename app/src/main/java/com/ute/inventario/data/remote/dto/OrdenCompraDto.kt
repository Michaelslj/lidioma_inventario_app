package com.ute.inventario.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.ute.inventario.domain.model.OrdenCompra
import com.ute.inventario.domain.model.OrdenCompraPayload
import com.ute.inventario.domain.model.ProductoDetalle

data class ProductoDetalleDto(
    val id: Int,
    val nombre: String,
    val precio: String
)

data class OrdenCompraDto(
    val id: Int,
    @SerializedName("codigo_orden") val codigoOrden: String,
    val proveedor: Int?,
    @SerializedName("proveedor_nombre") val proveedorNombre: String?,
    val usuario: String,
    val estado: String,
    @SerializedName("estado_display") val estadoDisplay: String,
    @SerializedName("total_estimado") val totalEstimado: Double,
    val productos: List<Int>,
    @SerializedName("productos_detalles") val productosDetalles: List<ProductoDetalleDto>,
    @SerializedName("creado_en") val creadoEn: String
)

data class OrdenCompraRequestDto(
    @SerializedName("codigo_orden") val codigoOrden: String,
    val proveedor: Int,
    val productos: List<Int>,
    val estado: String
)

// ── Mappers ───────────────────────────────────────────────────

fun ProductoDetalleDto.toDomain() = ProductoDetalle(
    id = id,
    nombre = nombre,
    precio = precio
)

fun OrdenCompraDto.toDomain() = OrdenCompra(
    id = id,
    codigoOrden = codigoOrden,
    proveedor = proveedor ?: 0,
    proveedorNombre = proveedorNombre ?: "Sin proveedor",
    usuario = usuario,
    estado = estado,
    estadoDisplay = estadoDisplay,
    totalEstimado = totalEstimado,
    productos = productos,
    productosDetalles = productosDetalles.map { it.toDomain() },
    creadoEn = creadoEn
)

fun OrdenCompraPayload.toRequest() = OrdenCompraRequestDto(
    codigoOrden = codigo_orden,
    proveedor = proveedor,
    productos = productos,
    estado = estado
)
