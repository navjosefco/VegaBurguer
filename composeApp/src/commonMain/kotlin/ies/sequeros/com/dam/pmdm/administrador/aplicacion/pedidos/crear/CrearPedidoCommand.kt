package ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.crear

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.LineaPedidoDTO

data class CrearPedidoCommand (

    val dependienteId: String?,
    val customerName: String?,
    val lineas: List<LineaPedidoDTO>
)