package ies.sequeros.com.dam.pmdm.administrador.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Producto(
    
    val id: String,
    val categoria_id: String,
    val name: String,
    val description: String,
    val price: Double,
    val image_path: String,
    val enabled: Boolean
)