package ies.sequeros.com.dam.pmdm.administrador.ui.categorias.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.CategoriaDTO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CategoriaFormViewModel(
    private val item: CategoriaDTO?,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CategoriaFormState(
            name = item?.name ?: "",
            image_path = item?.image_path ?: "",
            enabled = item?.enabled ?: true
        )
    )
    val uiState: StateFlow<CategoriaFormState> = _uiState.asStateFlow()

    val isFormValid: StateFlow<Boolean> = uiState.map { state ->
        state.nameError == null && 
        state.image_pathError == null &&
        !state.name.isBlank() &&
        !state.image_path.isBlank()
    }.stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = false)

    fun onNameChange(v: String) {
        _uiState.value = _uiState.value.copy(name = v, nameError = validateName(v))
    }

    fun onImagePathChange(v: String) {
        _uiState.value = _uiState.value.copy(image_path = v, image_pathError = validateImagePath(v))
    }

    fun onEnabledChange(v: Boolean) {
        _uiState.value = _uiState.value.copy(enabled = v)
    }
    
    fun clear() {
        _uiState.value = CategoriaFormState()
    }

    private fun validateName(name: String): String? {
        if (name.isBlank()) return "El nombre es obligatorio"
        return null
    }

    private fun validateImagePath(path: String): String? {
        if (path.isBlank()) return "La imagen es obligatoria"
        return null
    }

    fun validateAll(): Boolean {
        val s = _uiState.value
        val nameErr = validateName(s.name)
        val imgErr = validateImagePath(s.image_path)
        
        val newState = s.copy(nameError = nameErr, image_pathError = imgErr, submitted = true)
        _uiState.value = newState
        return nameErr == null && imgErr == null
    }

    fun submit(
        onSuccess: (CategoriaFormState) -> Unit,
        onFailure: ((CategoriaFormState) -> Unit)? = null
    ) {
        viewModelScope.launch {
            if (validateAll()) onSuccess(_uiState.value) else onFailure?.invoke(_uiState.value)
        }
    }
}
