package ies.sequeros.com.dam.pmdm.administrador.modelo

interface IPedidoRepositorio {

    suspend fun add(pedido: Pedido): Unit
    suspend fun delete(pedido: Pedido): Boolean
    suspend fun update(pedido: Pedido): Boolean
    suspend fun getAll(): List<Pedido>
    suspend fun getById(id: String): Pedido?
    suspend fun getByDependiente(dependienteId: String): List<Pedido>
}