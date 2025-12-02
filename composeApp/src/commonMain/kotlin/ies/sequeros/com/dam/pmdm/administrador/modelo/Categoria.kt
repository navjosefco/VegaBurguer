package ies.sequeros.com.dam.pmdm.administrador.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Categoria(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val habilitado: Boolean
)
