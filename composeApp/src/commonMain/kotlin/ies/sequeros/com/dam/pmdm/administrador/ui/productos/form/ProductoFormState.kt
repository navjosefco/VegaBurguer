package ies.sequeros.com.dam.pmdm.administrador.ui.productos.form

data class ProductoFormState(

    // Datos del formulario
    val categoria_id: String = "",
    val name: String = "",
    val description: String = "",
    val price: String = "", // String para facilitar la entrada de texto decimal
    val imagePath: String = "",
    val enabled: Boolean = true,

    // Errores (null = sin error)
    val categoriaIdError: String? = null,
    val nameError: String? = null,
    val descriptionError: String? = null,
    val priceError: String? = null,
    val imagePathError: String? = null,

    val submitted: Boolean = false

)