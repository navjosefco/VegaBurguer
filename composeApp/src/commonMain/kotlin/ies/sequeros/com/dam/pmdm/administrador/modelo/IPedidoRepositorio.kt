package ies.sequeros.com.dam.pmdm.administrador.modelo

interface IPedidoRepositorio {

    suspend fun getAll(): List<Pedido>
    suspend fun getById(id: String): Pedido?
    suspend fun add(pedido: Pedido)
    suspend fun update(pedido: Pedido)
    suspend fun delete(pedido: Pedido)
    suspend fun getByDependiente(dependienteId: String): List<Pedido>
}