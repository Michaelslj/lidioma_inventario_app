package com.ute.inventario.data.repository

import com.ute.inventario.data.remote.api.OrdenCompraApi
import com.ute.inventario.data.remote.dto.toDomain
import com.ute.inventario.data.remote.dto.toRequest
import com.ute.inventario.domain.model.OrdenCompra
import com.ute.inventario.domain.model.OrdenCompraPayload
import com.ute.inventario.domain.repository.OrdenCompraRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrdenCompraRepositoryImpl @Inject constructor(
    private val api: OrdenCompraApi,
) : OrdenCompraRepository {

    override suspend fun getOrdenes(page: Int?): Result<List<OrdenCompra>> = runCatching {
        val response = api.getOrdenes(page)
        if (response.isSuccessful) {
            response.body()?.results?.map { it.toDomain() } ?: throw Exception("Respuesta vacía")
        } else {
            throw Exception("Error ${response.code()}")
        }
    }

    override suspend fun getOrden(id: Int): Result<OrdenCompra> = runCatching {
        val response = api.getOrden(id)
        if (response.isSuccessful) {
            response.body()?.toDomain() ?: throw Exception("Orden no encontrada")
        } else {
            throw Exception("Error ${response.code()}")
        }
    }

    override suspend fun crearOrden(orden: OrdenCompraPayload): Result<OrdenCompra> = runCatching {
        val response = api.crearOrden(orden.toRequest())
        if (response.isSuccessful) {
            response.body()?.toDomain() ?: throw Exception("Error al procesar respuesta")
        } else {
            throw Exception("Error ${response.code()}")
        }
    }

    override suspend fun eliminarOrden(id: Int): Result<Unit> = runCatching {
        val response = api.eliminarOrden(id)
        if (!response.isSuccessful) {
            throw Exception("Error ${response.code()}")
        }
    }
}
