package com.ute.inventario.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.inventario.domain.model.Categoria
import com.ute.inventario.domain.model.CategoriaPayload
import com.ute.inventario.domain.repository.CategoriaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CategoriaUiState {
    data object Loading : CategoriaUiState
    data class Success(val categorias: List<Categoria>) : CategoriaUiState
    data class Error(val message: String) : CategoriaUiState
}

@HiltViewModel
class CategoriaViewModel @Inject constructor(
    private val repository: CategoriaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CategoriaUiState>(CategoriaUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        cargarCategorias()
    }

    fun cargarCategorias() {
        viewModelScope.launch {
            _uiState.value = CategoriaUiState.Loading
            repository.getCategorias()
                .onSuccess { lista ->
                    _uiState.value = CategoriaUiState.Success(lista)
                }
                .onFailure { error ->
                    _uiState.value = CategoriaUiState.Error(
                        error.message ?: "Error al cargar categorías"
                    )
                }
        }
    }

    fun crearCategoria(payload: CategoriaPayload, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.crearCategoria(payload)
                .onSuccess {
                    cargarCategorias()
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.value = CategoriaUiState.Error(error.message ?: "Error al crear categoría")
                }
        }
    }

    fun actualizarCategoria(id: Int, payload: CategoriaPayload, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.actualizarCategoria(id, payload)
                .onSuccess {
                    cargarCategorias()
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.value = CategoriaUiState.Error(error.message ?: "Error al actualizar")
                }
        }
    }

    fun eliminarCategoria(id: Int) {
        viewModelScope.launch {
            repository.eliminarCategoria(id)
                .onSuccess {
                    cargarCategorias()
                }
                .onFailure { error ->
                    _uiState.value = CategoriaUiState.Error(error.message ?: "Error al eliminar")
                }
        }
    }
}
