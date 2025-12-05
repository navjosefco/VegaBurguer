package ies.sequeros.com.dam.pmdm.administrador.infraestructura

import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.productos.BBDDRepositorioProductoJava
import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto

class BBDDProductoRepository(
    private val bbddRepositorioProductoJava: BBDDRepositorioProductoJava
):IProductoRepositorio {

    override suspend fun add(producto: Producto){

        bbddRepositorioProductoJava.add(producto)
    }

    override suspend fun delete(producto: Producto): Boolean {

        bbddRepositorioProductoJava.remove(producto)
        return true
    }

    override suspend fun remove(id: String): Boolean {

        bbddRepositorioProductoJava.remove(id)
        return true
    }

    override suspend fun update(producto: Producto): Boolean{

        bbddRepositorioProductoJava.update(producto)
        return true
    }

    override suspend fun getAll(): List<Producto>{

        return bbddRepositorioProductoJava.all
    }

    override suspend fun getById(id: String): Producto? {

        return bbddRepositorioProductoJava.findById(id)
    }

    override suspend fun getByCategoria(idCategoria: String): List<Producto> {

        return bbddRepositorioProductoJava.getByCat(idCategoria)
    }

    override suspend fun getByName(name: String): Producto? {

        return bbddRepositorioProductoJava.findByName(name)
    }
}

/*
interface IProductoRepositorio {

    *suspend fun add(producto: Producto): Unit
    *suspend fun delete(producto:Producto): Boolean
    *suspend fun remove(id:String): Boolean
    *suspend fun update(producto: Producto): Boolean
    suspend fun getAll(): List<Producto>
    suspend fun getById(id: String): Producto?
    suspend fun getByCategoria(idCategoria: String): List<Producto>
}
*/