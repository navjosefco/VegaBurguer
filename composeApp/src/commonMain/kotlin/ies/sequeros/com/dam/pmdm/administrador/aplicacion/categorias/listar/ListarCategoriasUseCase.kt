package ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar

import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.CategoriaDTO
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class ListarCategoriasUseCase(
    private val repositorio: ICategoriaRepositorio,
    private val almacenDatos: AlmacenDatos
) {
    suspend fun invoke(): List<CategoriaDTO> {
        return repositorio.getAll().map { 
             it.toDTO(almacenDatos.getAppDataDir() + "/categorias/") 
        }
    }
}
