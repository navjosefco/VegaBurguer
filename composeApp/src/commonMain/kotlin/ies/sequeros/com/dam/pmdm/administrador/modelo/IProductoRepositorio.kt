package ies.sequeros.com.dam.pmdm.administrador.modelo


interface IProductoRepositorio {

    suspend fun add(producto: Producto): Unit
    suspend fun delete(producto:Producto): Boolean
    suspend fun remove(id:String): Boolean
    suspend fun update(producto: Producto): Boolean
    suspend fun getAll(): List<Producto>
    suspend fun getById(id: String): Producto?
    suspend fun getByCategoria(idCategoria: String): List<Producto>
    suspend fun getByName(name: String): Producto?

}