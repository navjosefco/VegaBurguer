package ies.sequeros.com.dam.pmdm.tpv.aplicacion.categorias.listar

import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio

class ListarCategoriasUseCase(

    private val categoriaRepositorio: ICategoriaRepositorio

) {
    suspend fun invoke(): List<Categoria> {
        
        return categoriaRepositorio.getAll()
    }
}
