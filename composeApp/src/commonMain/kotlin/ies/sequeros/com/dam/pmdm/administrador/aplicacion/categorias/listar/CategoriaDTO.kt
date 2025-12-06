package ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar

data class CategoriaDTO(
    val id: String,
    val name: String,
    val image_path: String, 
    val enabled: Boolean
)
