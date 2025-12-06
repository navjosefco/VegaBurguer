package ies.sequeros.com.dam.pmdm.administrador.ui.categorias.form

data class CategoriaFormState(
    val name: String = "",
    val image_path: String = "",
    val enabled: Boolean = true,

    // Errores
    val nameError: String? = null,
    val image_pathError: String? = null,

    val submitted: Boolean = false
)
