package com.ute.inventario.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.inventario.domain.model.Proveedor
import com.ute.inventario.domain.model.ProveedorPayload
import com.ute.inventario.domain.repository.ProveedorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ProveedorUiState {
    data object Loading : ProveedorUiState
    data class Success(val proveedores: List<Proveedor>) : ProveedorUiState
    data class Error(val message: String) : ProveedorUiState
}

@HiltViewModel
class ProveedorViewModel @Inject constructor(
    private val repository: ProveedorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProveedorUiState>(ProveedorUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        cargarProveedores()
    }

    fun cargarProveedores() {
        viewModelScope.launch {
            _uiState.value = ProveedorUiState.Loading
            repository.getProveedores()
                .onSuccess { lista ->
                    _uiState.value = ProveedorUiState.Success(lista)
                }
                .onFailure { error ->
                    _uiState.value = ProveedorUiState.Error(
                        error.message ?: "Error al cargar proveedores"
                    )
                }
        }
    }

    fun crearProveedor(payload: ProveedorPayload, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.crearProveedor(payload)
                .onSuccess {
                    cargarProveedores()
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.value = ProveedorUiState.Error(error.message ?: "Error al crear proveedor")
                }
        }
    }

    fun actualizarProveedor(id: Int, payload: ProveedorPayload, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.actualizarProveedor(id, payload)
                .onSuccess {
                    cargarProveedores()
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.value = ProveedorUiState.Error(error.message ?: "Error al actualizar")
                }
        }
    }

    fun eliminarProveedor(id: Int) {
        viewModelScope.launch {
            repository.eliminarProveedor(id)
                .onSuccess {
                    cargarProveedores()
                }
                .onFailure { error ->
                    _uiState.value = ProveedorUiState.Error(error.message ?: "Error al eliminar")
                }
        }
    }
}
