package ies.sequeros.com.dam.pmdm.administrador.modelo

interface IProductoRepositorio {
    
    suspend fun getAll(): list<Producto>
    suspend fun getById(id: String): Producto?
    suspend fun getByCategoria(idCategoria: String): list<Producto>
    suspend fun add(producto: Producto)
    suspend fun update(producto: Producto)
    suspend fun delete(producto:Producto)
}