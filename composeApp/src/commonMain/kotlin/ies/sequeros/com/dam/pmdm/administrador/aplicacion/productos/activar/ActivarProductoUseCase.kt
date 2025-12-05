package ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.activar

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ProductoDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.toDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class ActivarProductoUseCase (

    private val productoRepo: IProductoRepositorio,
    private val almacenDatos: AlmacenDatos
)

{
    suspend fun invoke(productoCommand: ActivarProductoCommand): ProductoDTO{

        val item: Producto? = productoRepo.getById(productoCommand.id)

        if(item == null)
            throw IllegalArgumentException("El producto no está registrado")

        val actuProducto = item.copy(
            enabled = productoCommand.enabled
        )

        productoRepo.update(actuProducto)

        return actuProducto.toDTO(almacenDatos.getAppDataDir() + "/productos/")
    }
}