package ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.actualizar

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ProductoDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.toDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class ActualizarProductoUseCase(

    private val productoRepo: IProductoRepositorio,
    private val almacenDatos: AlmacenDatos
) {

    suspend fun invoke(productoCommand: ActualizarProductoCommand): ProductoDTO{

        val item: Producto? = productoRepo.getById(productoCommand.id)

        if(item == null)
            throw IllegalArgumentException ("El producto no existe")

        if(item.name != productoCommand.name && productoRepo.getByName(productoCommand.name) != null)
            throw IllegalArgumentException("El nombre ya está registrado")

        var nuevaImagePath: String? = null
        val itemDTO : ProductoDTO = item.toDTO(almacenDatos.getAppDataDir()+ "/productos/")

        if(itemDTO.image_path != productoCommand.image_path){
            almacenDatos.remove(itemDTO.image_path)
            nuevaImagePath = almacenDatos.copy(
                productoCommand.image_path,
                productoCommand.id,
                "/productos/"
                )
        }else{
            nuevaImagePath =  item.image_path
        }

        val actuProducto = item.copy(
            categoria_id = productoCommand.id,
            name = productoCommand.name,
            description = productoCommand.description,
            price = productoCommand.price,
            image_path = productoCommand.image_path,
            enabled = productoCommand.enabled
        )

        productoRepo.update(actuProducto)

        return actuProducto.toDTO(almacenDatos.getAppDataDir()+"/productos/")

    }


}