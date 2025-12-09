package ies.sequeros.com.dam.pmdm.administrador.modelo

interface  IDependienteRepositorio {
    suspend fun add(item:Dependiente):Unit
    suspend fun delete(item:Dependiente): Boolean
    suspend fun remove(id:String): Boolean
    suspend fun update(item:Dependiente): Boolean
    suspend fun getAll():List<Dependiente>
    suspend fun findByName(name:String): Dependiente?
    suspend fun getById(id:String):Dependiente?
    suspend fun cambiarContrasenya(id: String, oldPass: String, newPass: String)

    suspend fun login(nombre: String, password: String): Dependiente?
}