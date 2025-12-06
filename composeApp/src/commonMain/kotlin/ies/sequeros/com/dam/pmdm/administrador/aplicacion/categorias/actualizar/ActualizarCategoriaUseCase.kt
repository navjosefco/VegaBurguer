package ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.actualizar

import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.CategoriaDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.toDTO
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class ActualizarCategoriaUseCase(
    private val repositorio: ICategoriaRepositorio,
    private val almacenDatos: AlmacenDatos
) {
    suspend fun invoke(command: ActualizarCategoriaCommand): CategoriaDTO {
        // 1. Validar existencia
        val existing = repositorio.getById(command.id) 
            ?: throw Exception("No existe la categoría con id ${command.id}")

        // 2. Validar nombre único si se ha cambiado
        if (existing.name != command.name) {
             val duplicate = repositorio.getByName(command.name)
             if (duplicate != null) {
                 throw Exception("Ya existe otra categoría con el nombre ${command.name}")
             }
        }

        // 3. Gestionar imagen (Si cambia, borrar antigua y copiar nueva)
        var finalImagePath = existing.image_path // Usar image_path de la entidad
        if (command.image_path != existing.image_path) {
             // Copiar nueva
             finalImagePath = almacenDatos.copy(command.image_path, command.id, "/categorias/") ?: command.image_path
        }

        // 4. Actualizar Entidad
        val toUpdate = existing.copy(
            name = command.name,
            image_path = finalImagePath, // Actualizar image_path
            enabled = command.enabled
        )

        // 5. Persistir
        repositorio.update(toUpdate)
        return toUpdate.toDTO(almacenDatos.getAppDataDir() + "/categorias/")
    }
}
