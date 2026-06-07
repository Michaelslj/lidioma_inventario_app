package com.ute.inventario.data.repository

import com.ute.inventario.data.remote.api.MovimientoInventarioApi
import com.ute.inventario.data.remote.dto.toDomain
import com.ute.inventario.data.remote.dto.toRequest
import com.ute.inventario.domain.model.MovimientoInventario
import com.ute.inventario.domain.model.MovimientoInventarioPayload
import com.ute.inventario.domain.repository.MovimientoInventarioRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovimientoInventarioRepositoryImpl @Inject constructor(
    private val api: MovimientoInventarioApi,
) : MovimientoInventarioRepository {

    override suspend fun getMovimientos(
        page: Int?,
        productoId: Int?,
    ): Result<List<MovimientoInventario>> = runCatching {
        val response = api.getMovimientos(page, productoId)
        if (response.isSuccessful) {
            response.body()?.results?.map { it.toDomain() } ?: throw Exception("Respuesta vacía")
        } else {
            throw Exception("Error ${response.code()}")
        }
    }

    override suspend fun getMovimiento(id: Int): Result<MovimientoInventario> = runCatching {
        val response = api.getMovimiento(id)
        if (response.isSuccessful) {
            response.body()?.toDomain() ?: throw Exception("Movimiento no encontrado")
        } else {
            throw Exception("Error ${response.code()}")
        }
    }

    override suspend fun crearMovimiento(movimiento: MovimientoInventarioPayload): Result<MovimientoInventario> = runCatching {
        val response = api.crearMovimiento(movimiento.toRequest())
        if (response.isSuccessful) {
            response.body()?.toDomain() ?: throw Exception("Error al procesar respuesta")
        } else {
            val errorBody = response.errorBody()?.string() ?: ""
            throw Exception("Error ${response.code()}: $errorBody")
        }
    }

    override suspend fun eliminarMovimiento(id: Int): Result<Unit> = runCatching {
        val response = api.eliminarMovimiento(id)
        if (!response.isSuccessful) {
            throw Exception("Error ${response.code()}")
        }
    }
}
