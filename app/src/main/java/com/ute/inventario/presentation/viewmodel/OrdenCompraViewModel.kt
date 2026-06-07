package com.ute.inventario.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.inventario.domain.model.OrdenCompra
import com.ute.inventario.domain.model.OrdenCompraPayload
import com.ute.inventario.domain.repository.OrdenCompraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface OrdenCompraUiState {
    data object Loading : OrdenCompraUiState
    data class Success(val ordenes: List<OrdenCompra>) : OrdenCompraUiState
    data class Error(val message: String) : OrdenCompraUiState
}

@HiltViewModel
class OrdenCompraViewModel @Inject constructor(
    private val repository: OrdenCompraRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<OrdenCompraUiState>(OrdenCompraUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        cargarOrdenes()
    }

    fun cargarOrdenes() {
        viewModelScope.launch {
            _uiState.value = OrdenCompraUiState.Loading
            repository.getOrdenes()
                .onSuccess { lista ->
                    _uiState.value = OrdenCompraUiState.Success(lista)
                }
                .onFailure { error ->
                    _uiState.value = OrdenCompraUiState.Error(
                        error.message ?: "Error al cargar las órdenes de compra"
                    )
                }
        }
    }

    fun crearOrden(payload: OrdenCompraPayload, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.crearOrden(payload)
                .onSuccess {
                    cargarOrdenes()
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.value = OrdenCompraUiState.Error(error.message ?: "Error al crear la orden")
                }
        }
    }

    fun eliminarOrden(id: Int) {
        viewModelScope.launch {
            repository.eliminarOrden(id)
                .onSuccess {
                    cargarOrdenes()
                }
                .onFailure { error ->
                    _uiState.value = OrdenCompraUiState.Error(error.message ?: "Error al eliminar la orden")
                }
        }
    }
}
