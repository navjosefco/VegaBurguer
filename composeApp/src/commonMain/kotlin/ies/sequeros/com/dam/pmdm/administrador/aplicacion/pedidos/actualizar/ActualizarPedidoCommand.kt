package ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.actualizar

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.LineaPedidoDTO

data class ActualizarPedidoCommand (

    val id: String,
    val status: String,
    val customerName: String?,
    val dependienteId: String?,
    val lineas: List<LineaPedidoDTO>
)