package ies.sequeros.com.dam.pmdm.tpv.aplicacion.productos.listar

import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto

class ListarProductosPorCategoriaUseCase(
    private val productoRepositorio: IProductoRepositorio
) {
    /**
     * categoriaId ID de la categoría seleccionada por el usuario
     */
    suspend operator fun invoke(categoriaId: String): List<Producto> {

        return productoRepositorio.getAll().filter { 

            it.categoria_id == categoriaId && it.enabled 
        }
    }
}
