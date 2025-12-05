package ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.crear

data class CrearProductoCommand (

    val id: String,
    val categoria_id: String,
    val name: String,
    val description: String,
    val price: Double,
    val image_path: String,
    val enabled: Boolean
)