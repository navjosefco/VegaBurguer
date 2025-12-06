package ies.sequeros.com.dam.pmdm.administrador.infraestructura

import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.categorias.BBDDRepositorioCategoriaJava
import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BBDDCategoriaRepository(
    private val bbddRepositorioCategoriaJava: BBDDRepositorioCategoriaJava
): ICategoriaRepositorio {

    override suspend fun add(categoria: Categoria) {
        bbddRepositorioCategoriaJava.add(categoria)
    }

    override suspend fun delete(categoria: Categoria): Boolean {
        bbddRepositorioCategoriaJava.remove(categoria)
        return true
    }

    override suspend fun remove(id: String): Boolean {
        bbddRepositorioCategoriaJava.remove(id)
        return true
    }

    override suspend fun update(categoria: Categoria): Boolean {
        bbddRepositorioCategoriaJava.update(categoria)
        return true
    }

    override suspend fun getAll(): List<Categoria> {
        return bbddRepositorioCategoriaJava.all
    }

    override suspend fun getById(id: String): Categoria? {
        return bbddRepositorioCategoriaJava.findById(id)
    }

    override suspend fun getByName(name: String): Categoria? {
        return bbddRepositorioCategoriaJava.findByName(name)
    }
}
