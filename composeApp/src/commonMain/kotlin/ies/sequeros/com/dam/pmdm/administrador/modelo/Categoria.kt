package ies.sequeros.com.dam.pmdm.administrador.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Categoria(
    val id: String,
    val name: String,
    val image_path: String,
    val enabled: Boolean
)
