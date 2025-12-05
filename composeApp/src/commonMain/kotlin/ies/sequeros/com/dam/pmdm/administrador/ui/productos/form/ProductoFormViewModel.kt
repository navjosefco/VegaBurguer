package ies.sequeros.com.dam.pmdm.administrador.ui.productos.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ProductoDTO

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class ProductoFormViewModel(
    private val item: ProductoDTO?,
    onSuccess: (ProductoFormState) -> Unit): ViewModel() {

        //Me creo un MutableStateFlow con el estado inicial del formulario
        private val _uiState = MutableStateFlow(

            ProductoFormState(

                categoria_id = item?.categoria_id ?: "",
                name = item?.name ?: "",
                description = item?.description ?: "",
                price = item?.price.toString() ?: "",
                imagePath = item?.image_path ?: "",
                enabled = item?.enabled ?: true
            )
        )

    // Exponemos el estado del formulario como un StateFlow para que sea reactivo
    val uiState: StateFlow<ProductoFormState> = _uiState.asStateFlow()

    val isFormValid: StateFlow<Boolean> = uiState.map {state ->
        if(item==null)
            state.categoriaIdError == null &&
            state.nameError == null &&
            state.descriptionError == null &&
            state.priceError == null &&
            state.imagePathError == null &&

            !state.categoria_id.isBlank() &&
            !state.name.isBlank() &&
            !state.description.isBlank() &&
            !state.price.isBlank() &&
            !state.imagePath.isBlank()
        else {
            state.categoriaIdError == null &&
            state.nameError == null &&
            state.descriptionError == null &&
            state.priceError == null &&
            state.imagePathError == null &&
            !state.categoria_id.isBlank() &&
            !state.name.isBlank() &&
            !state.description.isBlank() &&
            !state.price.isBlank() &&
            !state.imagePath.isBlank()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )

    private fun validateName(name: String): String? {

        if (name.isBlank()) return "El nombre es obligatorio"
        if (name.length < 2) return "El nombre es muy corto"
        return null
    }

    private fun validatePrice(price: String): String? {

        if (price.isBlank()) return "El precio es obligatorio"

        val d = price.toDoubleOrNull()

        if (d == null) return "Debe ser un número válido"
        if (d < 0) return "El precio no puede ser negativo"

        return null
    }

    private fun validateImagePath(path: String): String? {

        if (path.isBlank()) return "La imagen es obligatoria"
        return null
    }

    fun validateAll(): Boolean {
        val s = _uiState.value
        val nameErr = validateName(s.name)
        val priceErr = validatePrice(s.price)
        val imgErr = validateImagePath(s.imagePath)

        val newState = s.copy(
            nameError = nameErr,
            priceError = priceErr,
            imagePathError = imgErr,
            submitted = true
        )
        _uiState.value = newState

        return listOf(nameErr, priceErr, imgErr).all { it == null }

    }

    fun submit(
        onSuccess: (ProductoFormState) -> Unit,
        onFailure: ((ProductoFormState) -> Unit)? = null
    ) {
        viewModelScope.launch {
            if (validateAll()) {
                onSuccess(_uiState.value)
            } else {
                onFailure?.invoke(_uiState.value)
            }
        }
    }

    fun onNameChange(v: String) {

        _uiState.value = _uiState.value.copy(name = v, nameError = validateName(v))
    }

    fun onPriceChange(v: String) {

        _uiState.value = _uiState.value.copy(price = v, priceError = validatePrice(v))
    }

    fun onDescriptionChange(v: String) {

        _uiState.value = _uiState.value.copy(description = v)
    }

    fun onImagePathChange(v: String) {

        _uiState.value = _uiState.value.copy(imagePath = v, imagePathError = validateImagePath(v))
    }

    fun onCategoriaChange(v: String) {

        _uiState.value = _uiState.value.copy(categoria_id = v)
    }

    fun onEnabledChange(v: Boolean) {

        _uiState.value = _uiState.value.copy(enabled = v)
    }

    fun clear() {

        _uiState.value = ProductoFormState()
    }
}