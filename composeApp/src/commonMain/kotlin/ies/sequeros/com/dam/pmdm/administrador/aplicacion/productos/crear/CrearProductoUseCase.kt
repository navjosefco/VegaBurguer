package ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.crear

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ProductoDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.toDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import ies.sequeros.com.dam.pmdm.generateUUID

class CrearProductoUseCase(

    private val productoRepo: IProductoRepositorio,
    private val almacenDatos: AlmacenDatos
) {

    suspend fun invoke(

        crearProductoCommand: CrearProductoCommand
    ): ProductoDTO{

        if(productoRepo.getByName(crearProductoCommand.name) != null) {
            throw IllegalArgumentException("El nombre ya está registrado")
        }

        if(crearProductoCommand.price < 0)
            throw Exception("El precio no puee ser un número negativo")

        if(crearProductoCommand.name.isBlank())
            throw Exception("El nombre es obligatorio")

        val id = generateUUID()

        val imageName = almacenDatos.copy(crearProductoCommand.image_path,id,"/productos/")

        val newProducto = Producto(
            id = id,
            categoria_id = crearProductoCommand.categoria_id,
            name = crearProductoCommand.name,
            description = crearProductoCommand.description,
            price = crearProductoCommand.price,
            image_path = imageName ?: crearProductoCommand.image_path,
            enabled = crearProductoCommand.enabled
        )

        if (productoRepo.getByName(newProducto.name) != null) {
            throw IllegalArgumentException("El nombre ya está registrado.")
        }

        productoRepo.add(newProducto)
        return newProducto.toDTO(almacenDatos.getAppDataDir()+"/productos/")

    }


}