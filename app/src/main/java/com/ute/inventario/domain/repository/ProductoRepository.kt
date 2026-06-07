package com.ute.inventario.domain.repository

import com.ute.inventario.domain.model.Producto
import com.ute.inventario.domain.model.ProductoPayload

interface ProductoRepository {
    suspend fun getProductos(filtros: Map<String, String> = emptyMap()): Result<List<Producto>>
    suspend fun getProducto(id: Int): Result<Producto>
    suspend fun crearProducto(payload: ProductoPayload): Result<Producto>
    suspend fun actualizarProducto(id: Int, payload: ProductoPayload): Result<Producto>
    suspend fun eliminarProducto(id: Int): Result<Unit>
}