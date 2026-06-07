package com.ute.inventario.data.repository

import com.ute.inventario.data.remote.api.ProductoApi
import com.ute.inventario.data.remote.dto.toDomain
import com.ute.inventario.data.remote.dto.toRequest
import com.ute.inventario.domain.model.Producto
import com.ute.inventario.domain.model.ProductoPayload
import com.ute.inventario.domain.repository.ProductoRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductoRepositoryImpl @Inject constructor(
    private val api: ProductoApi,
) : ProductoRepository {

    override suspend fun getProductos(filtros: Map<String, String>): Result<List<Producto>> = runCatching {
        val response = api.getProductos(filtros)
        if (response.isSuccessful) {
            response.body()?.results?.map { it.toDomain() } ?: throw Exception("Respuesta vacía")
        } else {
            throw Exception("Error ${response.code()}: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun getProducto(id: Int): Result<Producto> = runCatching {
        val response = api.getProducto(id)
        if (response.isSuccessful) {
            response.body()?.toDomain() ?: throw Exception("Producto no encontrado")
        } else {
            throw Exception("Error ${response.code()}")
        }
    }

    override suspend fun crearProducto(payload: ProductoPayload): Result<Producto> = runCatching {
        val response = api.crearProducto(payload.toRequest())
        if (response.isSuccessful) {
            response.body()?.toDomain() ?: throw Exception("Error al procesar respuesta")
        } else {
            throw Exception("Error ${response.code()}")
        }
    }

    override suspend fun actualizarProducto(id: Int, payload: ProductoPayload): Result<Producto> = runCatching {
        val response = api.actualizarProducto(id, payload.toRequest())
        if (response.isSuccessful) {
            response.body()?.toDomain() ?: throw Exception("Error al actualizar")
        } else {
            throw Exception("Error ${response.code()}")
        }
    }

    override suspend fun eliminarProducto(id: Int): Result<Unit> = runCatching {
        val response = api.eliminarProducto(id)
        if (!response.isSuccessful) {
            throw Exception("Error ${response.code()}")
        }
    }
}
