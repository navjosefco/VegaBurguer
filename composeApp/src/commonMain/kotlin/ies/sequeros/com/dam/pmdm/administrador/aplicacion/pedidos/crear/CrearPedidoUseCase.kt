package ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.crear

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.PedidoDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.toDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.LineaPedido
import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import ies.sequeros.com.dam.pmdm.generateUUID

class CrearPedidoUseCase(

    private val pedidoRepositorio: IPedidoRepositorio,
    private val almacenDatos: AlmacenDatos
) {

    suspend fun invoke(command: CrearPedidoCommand): PedidoDTO {
        // 1. Generar ID unico
        val pedidoId = generateUUID();

        // 2. Procesar Lineas y Calcular Total
        var totalCalculado = 0.0

        val lineasDominio = command.lineas.map { lineaDto ->

            val lineaTotal = lineaDto.quantity * lineaDto.unitPrice

            totalCalculado += lineaTotal

            LineaPedido(
                id = generateUUID(), // Cada linea tambien necesita su ID unico
                pedidoId = pedidoId,
                productoId = lineaDto.productoId,
                quantity = lineaDto.quantity,
                unitPrice = lineaDto.unitPrice
            )
        }

        // 3. Creamos el pedido
        val nuevoPedido = Pedido(
            id = pedidoId,
            status = "PENDIENTE", //Ya que se debe crear en estado Pendiente
            customerName = command.customerName,
            totalPrice = totalCalculado, // El total es el calculado aqui
            dependienteId = command.dependienteId,
            lineas = lineasDominio
        )

        // 4. Guardar en Base de Datos
        pedidoRepositorio.add(nuevoPedido)

        // 5. Devolver DTO del pedido creado (para confirmar a la UI)
        return nuevoPedido.toDTO()
    }
}