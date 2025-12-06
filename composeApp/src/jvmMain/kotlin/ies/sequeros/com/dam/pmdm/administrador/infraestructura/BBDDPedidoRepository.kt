package ies.sequeros.com.dam.pmdm.administrador.infraestructura

import ies.sequeros.com.dam.pmdm.administrador.infraestructura.pedidos.BBDDRepositorioPedidoJava
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido

class BBDDPedidoRepository(
    private val bbddRepositorioPedidoJava: BBDDRepositorioPedidoJava
): IPedidoRepositorio {

    override suspend fun add(pedido: Pedido){

        bbddRepositorioPedidoJava.add(pedido)
    }

    override suspend fun delete(pedido: Pedido): Boolean{

        bbddRepositorioPedidoJava.remove(pedido)
        return true
    }

    override suspend fun update(pedido: Pedido): Boolean{

        bbddRepositorioPedidoJava.update(pedido)
        return true
    }

    override suspend fun getAll(): List<Pedido>{

        return bbddRepositorioPedidoJava.all
    }

    override suspend fun getById(id: String): Pedido?{

        return bbddRepositorioPedidoJava.getById(id)
    }

    override suspend fun getByDependiente(dependienteId: String): List<Pedido> {

        return bbddRepositorioPedidoJava.getAll().filter { it.dependienteId == dependienteId }
    }

}