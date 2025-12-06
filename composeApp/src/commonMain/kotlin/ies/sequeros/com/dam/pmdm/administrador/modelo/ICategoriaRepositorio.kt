package ies.sequeros.com.dam.pmdm.administrador.modelo

interface ICategoriaRepositorio {
    suspend fun add(categoria: Categoria)
    suspend fun delete(categoria: Categoria): Boolean
    suspend fun remove(id: String): Boolean
    suspend fun update(categoria: Categoria): Boolean
    suspend fun getAll(): List<Categoria>
    suspend fun getById(id: String): Categoria?
    suspend fun getByName(name: String): Categoria?
}
