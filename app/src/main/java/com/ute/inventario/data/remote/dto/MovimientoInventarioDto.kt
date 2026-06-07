package com.ute.inventario.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.ute.inventario.domain.model.MovimientoInventario
import com.ute.inventario.domain.model.MovimientoInventarioPayload

data class MovimientoInventarioDto(
    val id: Int,
    val producto: Int,
    @SerializedName("producto_nombre") val productoNombre: String,
    @SerializedName("producto_categoria") val productoCategoria: String,
    val proveedor: Int?,
    @SerializedName("proveedor_nombre") val proveedorNombre: String?,
    val tipo: String,
    @SerializedName("tipo_display") val tipoDisplay: String,
    val cantidad: Int,
    val motivo: String,
    val usuario: String,
    @SerializedName("creado_en") val creadoEn: String
)

data class MovimientoInventarioCreateDto(
    val producto: Int,
    val proveedor: Int?,
    val tipo: String,
    val cantidad: Int,
    val motivo: String
)

// ── Mappers ───────────────────────────────────────────────────

fun MovimientoInventarioDto.toDomain() = MovimientoInventario(
    id = id,
    producto = producto,
    productoNombre = productoNombre,
    productoCategoria = productoCategoria,
    proveedor = proveedor,
    proveedorNombre = proveedorNombre,
    tipo = tipo,
    tipoDisplay = tipoDisplay,
    cantidad = cantidad,
    motivo = motivo,
    usuario = usuario,
    creadoEn = creadoEn
)

fun MovimientoInventarioPayload.toRequest() = MovimientoInventarioCreateDto(
    producto = producto,
    proveedor = proveedor,
    tipo = tipo,
    cantidad = cantidad,
    motivo = motivo
)
