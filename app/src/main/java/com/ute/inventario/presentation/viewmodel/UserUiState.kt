package com.ute.inventario.presentation.viewmodel

import com.ute.inventario.domain.model.User

sealed interface UserUiState {
    data object Loading : UserUiState
    data class Success(val user: User) : UserUiState
    data class ListSuccess(val users: List<User>) : UserUiState
    data class Error(val message: String) : UserUiState
}
