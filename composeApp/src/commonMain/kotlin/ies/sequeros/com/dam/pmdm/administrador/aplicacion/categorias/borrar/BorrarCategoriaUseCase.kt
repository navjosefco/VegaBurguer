package ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.borrar

import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class BorrarCategoriaUseCase(
    private val repositorio: ICategoriaRepositorio,
    private val almacenDatos: AlmacenDatos
) {
    suspend fun invoke(id: String) {
        val existing = repositorio.getById(id)
        if (existing != null) {
            // Borrar imagen asociada si es necesario
            // almacenDatos.delete(existing.image_path) // Usar image_path
            repositorio.remove(id)
        }
    }
}
