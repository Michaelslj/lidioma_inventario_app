package com.ute.inventario.data.repository

import com.ute.inventario.data.remote.api.ProveedorApi
import com.ute.inventario.data.remote.dto.toDomain
import com.ute.inventario.data.remote.dto.toRequest
import com.ute.inventario.domain.model.Proveedor
import com.ute.inventario.domain.model.ProveedorPayload
import com.ute.inventario.domain.repository.ProveedorRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProveedorRepositoryImpl @Inject constructor(
    private val api: ProveedorApi,
) : ProveedorRepository {

    override suspend fun getProveedores(page: Int?): Result<List<Proveedor>> = runCatching {
        val response = api.getProveedores(page)
        if (response.isSuccessful) {
            response.body()?.results?.map { it.toDomain() } ?: throw Exception("Respuesta vacía")
        } else {
            throw Exception("Error ${response.code()}")
        }
    }

    override suspend fun getProveedor(id: Int): Result<Proveedor> = runCatching {
        val response = api.getProveedor(id)
        if (response.isSuccessful) {
            response.body()?.toDomain() ?: throw Exception("Proveedor no encontrado")
        } else {
            throw Exception("Error ${response.code()}")
        }
    }

    override suspend fun crearProveedor(proveedor: ProveedorPayload): Result<Proveedor> = runCatching {
        val response = api.crearProveedor(proveedor.toRequest())
        if (response.isSuccessful) {
            response.body()?.toDomain() ?: throw Exception("Error al procesar respuesta")
        } else {
            throw Exception("Error ${response.code()}")
        }
    }

    override suspend fun actualizarProveedor(id: Int, proveedor: ProveedorPayload): Result<Proveedor> = runCatching {
        val response = api.actualizarProveedor(id, proveedor.toRequest())
        if (response.isSuccessful) {
            response.body()?.toDomain() ?: throw Exception("Error al actualizar")
        } else {
            throw Exception("Error ${response.code()}")
        }
    }

    override suspend fun eliminarProveedor(id: Int): Result<Unit> = runCatching {
        val response = api.eliminarProveedor(id)
        if (!response.isSuccessful) {
            throw Exception("Error ${response.code()}")
        }
    }
}
