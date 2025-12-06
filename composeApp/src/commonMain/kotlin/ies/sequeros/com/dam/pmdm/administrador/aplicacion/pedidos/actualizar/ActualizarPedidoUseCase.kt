package ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.actualizar


import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.PedidoDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.toDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.toLineaPedido
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class ActualizarPedidoUseCase (

    private val pedidoRepositorio: IPedidoRepositorio,
    private val almacenDatos: AlmacenDatos
){

    suspend fun invoke(command: ActualizarPedidoCommand): PedidoDTO{

        val lineasDPedido = command.lineas.map{ it.toLineaPedido(command.id)}

        val total = lineasDPedido.sumOf { it.quantity * it.unitPrice }

        val pedidoActualizado = Pedido(

            id = command.id,
            status = command.status,
            customerName = command.customerName,
            totalPrice = total, // Total recalculado
            dependienteId = command.dependienteId,
            lineas = lineasDPedido
        )

        pedidoRepositorio.update(pedidoActualizado)

        return pedidoActualizado.toDTO()
    }
}