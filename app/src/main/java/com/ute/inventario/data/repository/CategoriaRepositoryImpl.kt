package com.ute.inventario.data.repository

import com.ute.inventario.data.remote.api.CategoriaApi
import com.ute.inventario.data.remote.dto.toDomain
import com.ute.inventario.data.remote.dto.toRequest
import com.ute.inventario.domain.model.Categoria
import com.ute.inventario.domain.model.CategoriaPayload
import com.ute.inventario.domain.repository.CategoriaRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoriaRepositoryImpl @Inject constructor(
    private val api: CategoriaApi,
) : CategoriaRepository {

    override suspend fun getCategorias(): Result<List<Categoria>> = runCatching {
        val response = api.getCategorias()
        if (response.isSuccessful) {
            // Transformamos cada DTO a Modelo de dominio
            response.body()?.results?.map { it.toDomain() }
                ?: throw Exception("Respuesta vacía")
        } else {
            throw Exception("Error ${response.code()}: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun getCategoria(id: Int): Result<Categoria> = runCatching {
        val response = api.getCategoria(id)
        if (response.isSuccessful) {
            response.body()?.toDomain() ?: throw Exception("Categoría no encontrada")
        } else {
            throw Exception("Error ${response.code()}")
        }
    }

    override suspend fun crearCategoria(payload: CategoriaPayload): Result<Categoria> = runCatching {
        val response = api.crearCategoria(payload.toRequest())
        if (response.isSuccessful) {
            response.body()?.toDomain() ?: throw Exception("Error al procesar respuesta")
        } else {
            throw Exception("Error ${response.code()}: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun actualizarCategoria(id: Int, payload: CategoriaPayload): Result<Categoria> = runCatching {
        val response = api.actualizarCategoria(id, payload.toRequest())
        if (response.isSuccessful) {
            response.body()?.toDomain() ?: throw Exception("Error al actualizar")
        } else {
            throw Exception("Error ${response.code()}: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun eliminarCategoria(id: Int): Result<Unit> = runCatching {
        val response = api.eliminarCategoria(id)
        if (!response.isSuccessful) {
            throw Exception("Error ${response.code()}")
        }
    }
}