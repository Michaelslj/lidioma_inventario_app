package com.ute.inventario.domain.repository

import com.ute.inventario.domain.model.MovimientoInventario
import com.ute.inventario.domain.model.MovimientoInventarioPayload

interface MovimientoInventarioRepository {
    suspend fun getMovimientos(
        page: Int? = null,
        productoId: Int? = null
    ): Result<List<MovimientoInventario>>

    suspend fun getMovimiento(id: Int): Result<MovimientoInventario>

    suspend fun crearMovimiento(movimiento: MovimientoInventarioPayload): Result<MovimientoInventario>
    suspend fun eliminarMovimiento(id: Int): Result<Unit>
}
