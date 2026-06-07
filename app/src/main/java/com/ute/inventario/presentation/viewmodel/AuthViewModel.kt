package com.ute.inventario.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.inventario.domain.model.LoginRequest
import com.ute.inventario.domain.model.RegisterRequest
import com.ute.inventario.domain.model.User
import com.ute.inventario.domain.repository.AuthRepository
import com.ute.inventario.domain.repository.UserRepository
import com.ute.inventario.data.local.TokenDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val userRepository: UserRepository,
    private val tokenDataStore: TokenDataStore
) : ViewModel() {

    private val _isCheckingSession = MutableStateFlow(true)
    val isCheckingSession: StateFlow<Boolean> = _isCheckingSession.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            val authenticated = repository.estaAutenticado()
            if (authenticated) {
                _isAuthenticated.value = true
                val staffStatus = tokenDataStore.isStaff.first()
                
                // Cargamos datos temporales basados en lo guardado
                _currentUser.value = User(
                    id = 0, 
                    username = "Cargando...", 
                    email = "", 
                    nombreCompleto = "Cargando...", 
                    isStaff = staffStatus, 
                    isActive = true, 
                    fechaRegistro = ""
                )
                fetchProfileSync()
            } else {
                _isAuthenticated.value = false
                _currentUser.value = null
            }
            _isCheckingSession.value = false
        }
    }

    private suspend fun fetchProfileSync() {
        // Guardamos el rango de admin que ya tenemos (del login o del DataStore)
        val wasAdmin = _currentUser.value?.isStaff ?: tokenDataStore.isStaff.first()
        
        userRepository.getPerfil()
            .onSuccess { user -> 
                // Actualizamos datos pero PROTEGEMOS el rango de admin
                val finalAdminStatus = user.isStaff || wasAdmin
                _currentUser.value = user.copy(isStaff = finalAdminStatus) 
                tokenDataStore.saveStaffStatus(finalAdminStatus)
            }
            .onFailure {
                // Si el perfil falla, al menos mantenemos el rol que ya teníamos
                val current = _currentUser.value
                if (current?.username == "Cargando...") {
                    _currentUser.value = current.copy(username = "Usuario", nombreCompleto = "Sesión Activa", isStaff = wasAdmin)
                }
            }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            repository.login(LoginRequest(username, password))
                .onSuccess { tokenResponse ->
                    // El tokenResponse ya unifica staff y superuser en isStaff
                    val isAdmin = tokenResponse.isStaff
                    
                    _currentUser.value = User(
                        id = tokenResponse.userId,
                        username = tokenResponse.username,
                        email = "",
                        nombreCompleto = tokenResponse.username,
                        isStaff = isAdmin, 
                        isActive = true,
                        fechaRegistro = ""
                    )
                    
                    _isAuthenticated.value = true
                    
                    // El repo ya guardó en DataStore. Ahora intentamos perfil completo
                    fetchProfileSync()
                    
                    _uiState.value = AuthUiState.Success(tokenResponse)
                }
                .onFailure { error ->
                    _uiState.value = AuthUiState.Error(error.localizedMessage ?: "Error al iniciar sesión")
                }
        }
    }

    fun register(data: RegisterRequest) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            repository.register(data)
                .onSuccess { tokenResponse ->
                    _isAuthenticated.value = true
                    _currentUser.value = User(
                        id = tokenResponse.userId,
                        username = tokenResponse.username,
                        email = data.email,
                        nombreCompleto = tokenResponse.username,
                        isStaff = false, 
                        isActive = true,
                        fechaRegistro = ""
                    )
                    _uiState.value = AuthUiState.Success(tokenResponse)
                }
                .onFailure { error ->
                    _uiState.value = AuthUiState.Error(error.localizedMessage ?: "Error en el registro")
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _currentUser.value = null
            _isAuthenticated.value = false
            _uiState.value = AuthUiState.Idle
        }
    }
}
