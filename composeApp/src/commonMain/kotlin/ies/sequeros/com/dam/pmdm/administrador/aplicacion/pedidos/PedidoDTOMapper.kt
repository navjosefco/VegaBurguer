package ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.LineaPedidoDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.PedidoDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.LineaPedido
import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido

fun LineaPedido.toDTO(productoNombre: String = "", productoImage: String = "") = LineaPedidoDTO(

    id = id,
    productoId = productoId,
    productoNombre = productoNombre,
    productoImage = productoImage,
    quantity = quantity,
    unitPrice = unitPrice,
    total = total
)

fun LineaPedidoDTO.toLineaPedido(pedidoId: String) = LineaPedido(

    id = id,
    pedidoId = pedidoId,
    productoId = productoId,
    quantity = quantity,
    unitPrice = unitPrice
)

fun Pedido.toDTO() = PedidoDTO(

    id = id,
    status = status,
    customerName = customerName,
    totalPrice = totalPrice,
    dependienteId = dependienteId,
    /*Lineas es una lista, por lo que se puede usar el método map para convertir 
    cada elemento de la lista a un objeto LineaPedidoDTO*/
    lineas = lineas.map { it.toDTO() }
)

fun PedidoDTO.toPedido() = Pedido(

    id = id,
    status = status,
    customerName = customerName,
    totalPrice = totalPrice,
    dependienteId = dependienteId,
    lineas = lineas.map { it.toLineaPedido(id) }
)