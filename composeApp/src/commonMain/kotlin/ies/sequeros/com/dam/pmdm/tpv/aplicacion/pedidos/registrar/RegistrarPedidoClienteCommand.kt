package ies.sequeros.com.dam.pmdm.tpv.aplicacion.pedidos.registrar

import ies.sequeros.com.dam.pmdm.administrador.modelo.LineaPedido

data class RegistrarPedidoClienteCommand(

    val clienteNombre: String,
    val itemsCarrito: List<LineaPedido> 
)
