package ies.sequeros.com.dam.pmdm.administrador.ui.dependientes.formContrasenya

data class CambiarClaveFormState(
    // datos del formulario
    val oldPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",

    // errores (null = sin error) en el caso de que no se cumplan las validaciones
    val oldPasswordError: String? = null,
    val newPasswordError: String? = null,
    val confirmPasswordError: String? = null,

    // para controlar si se intentó enviar (mostrar errores globales)
    val submitted: Boolean = false
)