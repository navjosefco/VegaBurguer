package ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.crear

import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.CategoriaDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.toDTO
import ies.sequeros.com.dam.pmdm.generateUUID

class CrearCategoriaUseCase(
    private val repositorio: ICategoriaRepositorio,
    private val almacenDatos: AlmacenDatos
) {
    suspend fun invoke(command: CrearCategoriaCommand): CategoriaDTO {

        val existing = repositorio.getByName(command.name)

        if (existing != null) {

            throw Exception("Ya existe una categoría con el nombre ${command.name}")
        }

        val id = generateUUID()

        val newImagePath = almacenDatos.copy(command.image_path, id, "/categorias/")

        val categoria = Categoria(
            id = id,
            name = command.name,
            image_path = newImagePath ?: command.image_path, // Asignar a image_path
            enabled = command.enabled
        )

        repositorio.add(categoria)
        return categoria.toDTO(almacenDatos.getAppDataDir() + "/categorias/")
    }
}
