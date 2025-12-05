package ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.borrar

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.toDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class BorrarProductoUseCase (

    private val productoRepo: IProductoRepositorio,
    private val almacenDatos: AlmacenDatos
){
    suspend fun invoke(id: String){

        val productoTempo = productoRepo.getById(id)

        val productoElementos = productoRepo.getAll()

        if(productoTempo == null)
            throw IllegalArgumentException("El producto no existe")

        val tempoDTO = productoTempo.toDTO(almacenDatos.getAppDataDir()+"/dependientes/")

        productoRepo.remove(id)
        almacenDatos.remove(tempoDTO.image_path)
    }
}