package ies.sequeros.com.dam.pmdm.administrador.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Producto(
    
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagen: String,
    val idCategoria: String,
    val habilitado: Boolean
)