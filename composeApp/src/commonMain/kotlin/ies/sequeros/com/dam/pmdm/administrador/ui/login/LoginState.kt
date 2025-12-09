package ies.sequeros.com.dam.pmdm.administrador.ui.login

data class LoginState(
    
    val nombre: String = "",
    val contrasenya: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoginExitoso: Boolean = false
)
