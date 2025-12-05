package ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.activar

import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.CategoriaDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.toDTO
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class ActivarCategoriaUseCase(
    private val repositorio: ICategoriaRepositorio,
    private val almacenDatos: AlmacenDatos
) {
    suspend fun invoke(command: ActivarCategoriaCommand): CategoriaDTO {
        val existing = repositorio.getById(command.id)
            ?: throw Exception("Categoría no encontrada")
            
        val updated = existing.copy(enabled = command.enabled)
        repositorio.update(updated)
        return updated.toDTO()
    }
}
