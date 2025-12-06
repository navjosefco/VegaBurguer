package ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.crear

data class CrearCategoriaCommand(
    val name: String,
    val image_path: String,
    val enabled: Boolean
    // ID se genera en el UseCase
)
