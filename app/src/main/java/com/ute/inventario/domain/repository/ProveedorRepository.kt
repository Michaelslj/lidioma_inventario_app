package com.ute.inventario.domain.repository

import com.ute.inventario.domain.model.Proveedor
import com.ute.inventario.domain.model.ProveedorPayload

interface ProveedorRepository {
    suspend fun getProveedores(page: Int? = null): Result<List<Proveedor>>
    suspend fun getProveedor(id: Int): Result<Proveedor>
    suspend fun crearProveedor(proveedor: ProveedorPayload): Result<Proveedor>
    suspend fun actualizarProveedor(id: Int, proveedor: ProveedorPayload): Result<Proveedor>
    suspend fun eliminarProveedor(id: Int): Result<Unit>
}
