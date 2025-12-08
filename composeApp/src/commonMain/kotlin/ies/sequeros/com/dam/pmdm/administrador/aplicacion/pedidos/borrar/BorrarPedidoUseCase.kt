package ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.borrar

import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class BorrarPedidoUseCase(
    private val pedidoRepositorio: IPedidoRepositorio,
    private val almacenDatos: AlmacenDatos
) {

    suspend fun invoke(id: String){

        val pedido = pedidoRepositorio.getById(id)

        if(pedido != null) pedidoRepositorio.delete(pedido)
    }
}