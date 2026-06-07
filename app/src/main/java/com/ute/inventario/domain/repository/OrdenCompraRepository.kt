package com.ute.inventario.domain.repository

import com.ute.inventario.domain.model.OrdenCompra
import com.ute.inventario.domain.model.OrdenCompraPayload

interface OrdenCompraRepository {
    suspend fun getOrdenes(page: Int? = null): Result<List<OrdenCompra>>
    suspend fun getOrden(id: Int): Result<OrdenCompra>
    suspend fun crearOrden(orden: OrdenCompraPayload): Result<OrdenCompra>
    suspend fun eliminarOrden(id: Int): Result<Unit>
}
