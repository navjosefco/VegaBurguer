package ies.sequeros.com.dam.pmdm.tpv.aplicacion.pedidos.registrar

import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido
import ies.sequeros.com.dam.pmdm.generateUUID

class RegistrarPedidoClienteUseCase(

    private val pedidoRepositorio: IPedidoRepositorio
) {
    suspend operator fun invoke(command: RegistrarPedidoClienteCommand): Pedido {
        
        val pedidoId = generateUUID()
        
        // 1. Transformar líneas temporales a definitivas con IDs
        val lineasDefinitivas = command.itemsCarrito.map { lineaTemp ->
        
            lineaTemp.copy(
                id = generateUUID(),
                pedidoId = pedidoId
            )
        }
        
        // 2. Calcular total
        val totalPedido = lineasDefinitivas.sumOf { it.total }

        // 3. Crear entidad Pedido
        val nuevoPedido = Pedido(
            
            id = pedidoId,
            status = "PENDIENTE",
            customerName = command.clienteNombre,
            totalPrice = totalPedido,
            dependienteId = null,
            lineas = lineasDefinitivas
        )

        // 4. Persistir
        pedidoRepositorio.add(nuevoPedido)
        
        return nuevoPedido
    }
}
