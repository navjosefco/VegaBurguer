package ies.sequeros.com.dam.pmdm.administrador.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.login.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(

    private val loginUseCase: LoginUseCase

) : ViewModel() {

    //private val _uiState = MutableStateFlow(LoginState())
    //Borrar esto luego
    private val _uiState = MutableStateFlow(
        LoginState(
            nombre = "admin",
            contrasenya = "Admin123"
        )
    )

    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    fun onNombreChange(nuevoNombre: String) {

        _uiState.update { it.copy(nombre = nuevoNombre, error = null) }
    }

    fun onPasswordChange(nuevaPass: String) {

        _uiState.update { it.copy(contrasenya = nuevaPass, error = null) }
    }

    fun onLoginClick() {

        val state = _uiState.value
        if (state.nombre.isBlank() || state.contrasenya.isBlank()) {
            _uiState.update { it.copy(error = "Rellena todos los campos") }
            return
        }

        viewModelScope.launch {
            
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            // Llamada al Caso de Uso
            val resultado = loginUseCase(state.nombre, state.contrasenya)
            
            resultado.onSuccess { usuario ->
                if (usuario.isAdmin) {
                     _uiState.update { it.copy(isLoading = false, isLoginExitoso = true) }
                } else {
                     _uiState.update { it.copy(isLoading = false, error = "Acceso denegado: No eres administrador") }
                }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, error = "Credenciales incorrectas") }
            }
        }
    }
    
    // Método para resetear estado al navegar fuera
    fun resetState() {
        _uiState.value = LoginState()
    }
}
