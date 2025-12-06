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
        // 1. Validar nombre único
        val existing = repositorio.getByName(command.name)
        if (existing != null) {
            throw Exception("Ya existe una categoría con el nombre ${command.name}")
        }

        // 2. Generar ID
        val id = generateUUID()

        // 3. Copiar imagen al almacén (Lógica estricta de copia)
        val newImagePath = almacenDatos.copy(command.image_path, id, "/categorias/")

        // 4. Crear Entidad
        val categoria = Categoria(
            id = id,
            name = command.name,
            image_path = newImagePath ?: command.image_path, // Asignar a image_path
            enabled = command.enabled
        )

        // 5. Persistir
        repositorio.add(categoria) // Changed from save() to add() to match interface
        return categoria.toDTO(almacenDatos.getAppDataDir() + "/categorias/") // Manually mapping because add returns Unit
    }
}
