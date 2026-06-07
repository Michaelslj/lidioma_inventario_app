package com.ute.inventario.presentation.viewmodel

import com.ute.inventario.domain.model.TokenResponse

sealed interface AuthUiState {
    data object Idle : AuthUiState
    data object Loading : AuthUiState
    data class Success(val user: TokenResponse) : AuthUiState
    data class Error(val message: String) : AuthUiState
}
