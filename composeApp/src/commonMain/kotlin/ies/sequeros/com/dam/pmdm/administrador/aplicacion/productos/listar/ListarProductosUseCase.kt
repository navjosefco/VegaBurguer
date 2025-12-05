package ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.dependientes.listar.DependienteDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class ListarProductosUseCase(

    private val productoRepo: IProductoRepositorio,
    private val almacenDatos: AlmacenDatos
) {

    suspend fun invoke(): List<ProductoDTO> {

        val items = productoRepo.getAll().map { it.toDTO(if(it.image_path.isEmpty()) "" else almacenDatos.getAppDataDir()+"/productos/")}
        return items
    }

}