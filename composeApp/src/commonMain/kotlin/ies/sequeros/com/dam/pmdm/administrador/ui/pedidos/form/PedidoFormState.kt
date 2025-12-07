package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.form

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.LineaPedidoDTO

data class PedidoFormState(
    //Datos del formulario
    val id: String = "",
    val customerName: String = "",
    val status : String = "PENDIENTE",

    //Lista temporal de productos que se van añadiendo
    val lineas: List<LineaPedidoDTO> = emptyList(),

    //Errores
    val customerNameError: String? = null,
    val lineasError: String? = null,

    //Control para saber si se ha intentado enviar
    val submitted: Boolean = false
) 