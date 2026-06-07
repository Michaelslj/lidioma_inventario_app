package com.ute.inventario.domain.repository

import com.ute.inventario.domain.model.Categoria
import com.ute.inventario.domain.model.CategoriaPayload

interface CategoriaRepository {
    suspend fun getCategorias(): Result<List<Categoria>>
    suspend fun getCategoria(id: Int): Result<Categoria>
    suspend fun crearCategoria(payload: CategoriaPayload): Result<Categoria>
    suspend fun actualizarCategoria(id: Int, payload: CategoriaPayload): Result<Categoria>
    suspend fun eliminarCategoria(id: Int): Result<Unit>
}