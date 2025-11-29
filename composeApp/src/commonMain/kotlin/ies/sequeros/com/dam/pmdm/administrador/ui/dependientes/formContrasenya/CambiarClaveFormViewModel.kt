package ies.sequeros.com.dam.pmdm.administrador.ui.dependientes.formContrasenya

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


//:ViewModel -> Indicamos que es un ViewModel para la pantalla de cambiar contrasenya

class CambiarClaveFormViewModel : ViewModel() {
    
    //MutableStateFlow: para indicar que puede cambiar su estado 
    private val _uiState = MutableStateFlow(CambiarClaveFormState())

    //StateFlow: para indicar que es un flujo de datos
    //_uiState: estado del formulario y utilizamos _ para indicar que es privado
    val uiState: StateFlow<CambiarClaveFormState> = _uiState.asStateFlow()

     //isFormValid: Esta variable es un flujo de estado (StateFlow) que le dice a la interfaz gráfica (UI) 
     //             si el formulario es válido o no (True/False). Si es false, el botón de guardar se
     //             deshabilita.

    //uiState.map { state -> ... }:
    //   *Esto significa: "Escucha los cambios en uiState".
    //   *Cada vez que el usuario escribe una letra (y cambia el uiState), se ejecuta el código dentro de las llaves {}.
    //   *Transformación: Convierte el objeto state (que tiene textos y errores) en un simple Boolean (válido/inválido).

    //Las condiciones (&&):
    //   *Verifica que NO haya errores (error == null).
    //   *Verifica que los campos NO estén vacíos (isNotBlank()).
    //   *Solo si TODAS las condiciones se cumplen, devuelve true.

    //.stateIn(...):
    //   *La función map devuelve un flujo "frío" (que se apaga si nadie mira). stateIn lo convierte en un StateFlow "caliente" (siempre activo y guardando el último valor).
    //   *scope = viewModelScope: Vive y muere con el ViewModel. Si cierras la pantalla, deja de calcular.
    //   *started = SharingStarted.Eagerly: Empieza a calcular inmediatamente, no espera a que la UI lo pinte.
    //   *initialValue = false: Antes de que se ejecute la primera vez, asume que el formulario NO es válido.

    val isFormValid: StateFlow<Boolean> = uiState.map { state ->

        state.oldPasswordError == null &&
        state.newPasswordError == null &&
        state.confirmPasswordError == null &&
        state.oldPassword.isNotBlank() &&
        state.newPassword.isNotBlank() &&
        state.confirmPassword.isNotBlank()

    }.stateIn(
    
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )

    //Para este caso, la función se llama cada vez que el usuario escribe un carácter en el campo de "Contraseña Antigua".
    //v: String: Es el nuevo texto que el usuario acaba de escribir.
    //_uiState.value: Accedemos al estado actual (antes del cambio).
    //.copy(...):
    //   *En Kotlin, el estado es inmutable (no se puede cambiar una propiedad directamente).
    //   *copy crea un OBJETO NUEVO idéntico al anterior, pero cambiando solo las propiedades que le digas.
    //oldPassword = v: En el nuevo objeto, guardamos el texto nuevo.
    //oldPasswordError = validateOldPassword(v):
    //   *Ejecutamos la validación en tiempo real.
    //   *Si v está vacío, validateOldPassword devolverá un error ("Campo obligatorio").
    //   *Si está bien, devolverá null.
    //   *El resultado se guarda en la propiedad oldPasswordError del nuevo objeto.
    //_uiState.value = ...: Al asignar el NUEVO objeto al _uiState, el StateFlow emite un aviso a la UI diciendo que el estado ha cambiado.
    fun onOldPasswordChange(v: String){
        
        _uiState.value = _uiState.value.copy(
            oldPassword = v,
            oldPasswordError = validateOldPassword(v)
        )
    }

    //Valida la contraseña nueva
    fun onNewPasswordChange(v: String){
        
        _uiState.value = _uiState.value.copy(
            newPassword = v,
            newPasswordError = validateNewPassword(v)
        )
    }

    //Valida la contraseña de confirmación
    fun onConfirmPasswordChange(v: String){

        _uiState.value = _uiState.value.copy(
            confirmPassword = v,
            confirmPasswordError = validateConfirmPassword(
                uiState.value.newPassword // Pasamos la contraseña nueva
                ,v // Pasamos la contraseña de confirmación
                )
        )
    }

    //Con esta funcion limpiamos el formulario
    fun clear(){

        _uiState.value = CambiarClaveFormState()
    }

    //Valida la contraseña actual y devuelve el error
    private fun validateOldPassword(pw: String): String?{

         return if(pw.isBlank()) "La nueva contraseña actual es necesaria" else null
    }

    //Valida la contraseña nueva y devuelve el error
    private fun validateNewPassword(pw: String): String? {

        if (pw.isBlank()) return "La contraseña nueva es necesaria" 

        if (pw.length < 8) return "La contraseña nueva debe tener al menos 8 caracteres"

        val tieneNumero = pw.any { it.isDigit() }
        val tieneMayus = pw.any { it.isUpperCase() }

        if ( !tieneNumero || !tieneMayus) return "La contraseña nueva debe tener al menos un numero y una mayuscula"

        return null
    }

    //Compara la contraseña nueva con la de confirmación y devuelve el error
    private fun validateConfirmPassword(pw: String, confirm: String ): String? {

        if (confirm.isBlank()) return "Confirma la contraseña nueva"

        if (pw != confirm) return "Las contraseñas no coinciden"

        return null
    }

    //Valida todo el formulario
    fun validateAll(): Boolean{

        val s = _uiState.value
        val oldErr = validateOldPassword(s.oldPassword)
        val newErr = validateNewPassword(s.newPassword)
        val confirmErr = validateConfirmPassword(s.newPassword, s.confirmPassword)

        //Recoje los errores y los asigna al estado
        _uiState.value = _uiState.value.copy(

            oldPasswordError = oldErr,
            newPasswordError = newErr,
            confirmPasswordError = confirmErr,
            submitted = true
        )

        //Comprueba si todos los errores son nulos y si es asi, devuelve true
        return listOf(oldErr, newErr, confirmErr).all { it == null }
    }


    fun onSubmit(
        
        onSuccess: (String, String) -> Unit,
        onFailure: ((CambiarClaveFormState) -> Unit)? = null
    ){

        viewModelScope.launch{
            if(validateAll()){

                onSuccess(_uiState.value.oldPassword, _uiState.value.newPassword)
            }else{

                onFailure?.invoke(_uiState.value)
            }
        }
    }




}