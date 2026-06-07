package com.ute.inventario.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.ute.inventario.domain.model.Producto
import com.ute.inventario.domain.model.ProductoPayload

data class CategorySummaryDto(
    val id: Int,
    val nombre: String,
)

data class ProductDto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: String, // Django SerializerDecimalField devuelve String
    @SerializedName("precio_con_impuesto") val precioConImpuesto: Double,
    val stock: Int,
    @SerializedName("en_stock") val enStock: Boolean,
    @SerializedName("es_activo") val esActivo: Boolean,
    val categoria: CategorySummaryDto?,
)

data class ProductRequestDto(
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    @SerializedName("es_activo") val esActivo: Boolean,
    @SerializedName("categoria_id") val categoriaId: Int,
)

// ── Mappers ───────────────────────────────────────────────────

fun ProductDto.toDomain() = Producto(
    id = id,
    nombre = nombre,
    descripcion = descripcion,
    precio = precio.toDoubleOrNull() ?: 0.0,
    precioConImpuesto = precioConImpuesto,
    stock = stock,
    enStock = enStock,
    esActivo = esActivo,
    categoriaId = categoria?.id,
    categoriaNombre = categoria?.nombre,
)

fun ProductoPayload.toRequest() = ProductRequestDto(
    nombre = nombre,
    descripcion = descripcion,
    precio = precio,
    stock = stock,
    esActivo = esActivo,
    categoriaId = categoriaId,
)