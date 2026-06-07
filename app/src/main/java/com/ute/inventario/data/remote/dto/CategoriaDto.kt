package com.ute.inventario.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.ute.inventario.domain.model.Categoria
import com.ute.inventario.domain.model.CategoriaPayload
import java.text.Normalizer

data class CategoryDto(
    val id: Int,
    val nombre: String,
    val slug: String,
    val descripcion: String,
    val activa: Boolean,
    @SerializedName("total_productos") val totalProductos: Int,
    @SerializedName("creado_en") val creadoEn: String,
)

data class CategoryRequestDto(
    val nombre: String,
    val slug: String = "",
    val descripcion: String,
    val activa: Boolean,
)

data class CategoryStatsDto(
    val total: Int,
    val active: Int,
    val inactive: Int,
)

// ── Mappers ───────────────────────────────────────────────────

fun CategoryDto.toDomain() = Categoria(
    id = id,
    nombre = nombre,
    slug = slug,
    descripcion = descripcion,
    activa = activa,
    totalProductos = totalProductos,
    creadoEn = creadoEn,
)

fun CategoriaPayload.toRequest(): CategoryRequestDto {
    // Generar un slug válido para Django: sin acentos, minúsculas, guiones en lugar de espacios
    val normalized = Normalizer.normalize(nombre, Normalizer.Form.NFD)
        .replace("[^\\p{ASCII}]".toRegex(), "")
        .lowercase()
        .replace("[^a-z0-9\\s-]".toRegex(), "")
        .trim()
        .replace("\\s+".toRegex(), "-")
    
    return CategoryRequestDto(
        nombre = nombre,
        slug = normalized.ifBlank { "cat-${System.currentTimeMillis()}" },
        descripcion = descripcion,
        activa = activa,
    )
}
