package ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.activar

import jdk.jfr.Enabled

data class ActivarProductoCommand (

    val id: String,
    val enabled: Boolean
)