package com.ute.inventario.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.inventario.domain.model.Producto
import com.ute.inventario.domain.model.ProductoPayload
import com.ute.inventario.domain.repository.ProductoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ProductoUiState {
    data object Loading : ProductoUiState
    data class Success(val productos: List<Producto>) : ProductoUiState
    data class Error(val message: String) : ProductoUiState
}

@HiltViewModel
class ProductoViewModel @Inject constructor(
    private val repository: ProductoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductoUiState>(ProductoUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        obtenerProductos()
    }

    fun obtenerProductos() {
        viewModelScope.launch {
            _uiState.value = ProductoUiState.Loading
            repository.getProductos(emptyMap())
                .onSuccess { lista ->
                    _uiState.value = ProductoUiState.Success(lista)
                }
                .onFailure { error ->
                    _uiState.value = ProductoUiState.Error(error.message ?: "Error desconocido")
                }
        }
    }

    fun crearProducto(payload: ProductoPayload, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.crearProducto(payload)
                .onSuccess {
                    obtenerProductos()
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.value = ProductoUiState.Error(error.message ?: "Error al crear producto")
                }
        }
    }

    fun actualizarProducto(id: Int, payload: ProductoPayload, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.actualizarProducto(id, payload)
                .onSuccess {
                    obtenerProductos()
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.value = ProductoUiState.Error(error.message ?: "Error al actualizar")
                }
        }
    }

    fun eliminarProducto(id: Int) {
        viewModelScope.launch {
            repository.eliminarProducto(id)
                .onSuccess {
                    obtenerProductos()
                }
                .onFailure { error ->
                    _uiState.value = ProductoUiState.Error(error.message ?: "Error al eliminar")
                }
        }
    }
}
