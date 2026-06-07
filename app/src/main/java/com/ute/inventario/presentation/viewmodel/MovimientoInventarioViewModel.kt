package com.ute.inventario.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.inventario.domain.model.MovimientoInventario
import com.ute.inventario.domain.model.MovimientoInventarioPayload
import com.ute.inventario.domain.repository.MovimientoInventarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MovimientoInventarioUiState {
    data object Loading : MovimientoInventarioUiState
    data class Success(val movimientos: List<MovimientoInventario>) : MovimientoInventarioUiState
    data class Error(val message: String) : MovimientoInventarioUiState
}

@HiltViewModel
class MovimientoInventarioViewModel @Inject constructor(
    private val repository: MovimientoInventarioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovimientoInventarioUiState>(MovimientoInventarioUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        cargarMovimientos()
    }

    fun cargarMovimientos() {
        viewModelScope.launch {
            _uiState.value = MovimientoInventarioUiState.Loading
            repository.getMovimientos()
                .onSuccess { lista ->
                    _uiState.value = MovimientoInventarioUiState.Success(lista)
                }
                .onFailure { error ->
                    _uiState.value = MovimientoInventarioUiState.Error(
                        error.message ?: "Error al cargar movimientos"
                    )
                }
        }
    }

    fun crearMovimiento(payload: MovimientoInventarioPayload, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.crearMovimiento(payload)
                .onSuccess {
                    cargarMovimientos()
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.value = MovimientoInventarioUiState.Error(error.message ?: "Error al registrar movimiento")
                }
        }
    }

    fun eliminarMovimiento(id: Int) {
        viewModelScope.launch {
            repository.eliminarMovimiento(id)
                .onSuccess {
                    cargarMovimientos()
                }
                .onFailure { error ->
                    _uiState.value = MovimientoInventarioUiState.Error(error.message ?: "Error al eliminar")
                }
        }
    }
}
