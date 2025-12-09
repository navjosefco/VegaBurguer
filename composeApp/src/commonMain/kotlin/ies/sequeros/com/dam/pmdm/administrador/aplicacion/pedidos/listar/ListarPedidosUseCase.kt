package ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.toDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class ListarPedidosUseCase(
    private val pedidoRepositorio: IPedidoRepositorio,
    private val productoRepositorio: IProductoRepositorio,
    private val almacenDatos: AlmacenDatos 
) {
    suspend fun invoke(): List<PedidoDTO> {

        // 1. Obtener todos los pedidos de base de datos
        val listaPedidosDominio = pedidoRepositorio.getAll()
        
        // 2. Crear una lista para guardar los resultados
        val listaResultados = mutableListOf<PedidoDTO>()

        // 3. Recorrer cada pedido uno a uno
        for (pedido in listaPedidosDominio) {
            
            // 4. Procesar el pedido individualmente (convertir y enriquecer)
            val pedidoProcesado = procesarPedido(pedido)
            
            // 5. Añadir a la lista final
            listaResultados.add(pedidoProcesado)
        }
        
        return listaResultados
    }

    // Funcion auxiliar privada para limpiar el codigo principal
    private suspend fun procesarPedido(pedido: Pedido): PedidoDTO {
        // A. Convertir la cabecera del pedido
        val pedidoDTOBase = pedido.toDTO()
        
        // B. Creamos una lista para las lineas de este pedido
        val datosRequeridos = mutableListOf<LineaPedidoDTO>()

        // C. Recorrer las lineas del pedido original
        for (linea in pedido.lineas) {
            
            // D. Buscar informacion extra que necesitamos del repositorio del producto (Nombre, Imagen)
            val producto = productoRepositorio.getById(linea.productoId)
            val nombreProducto = producto?.name ?: "Producto Desconocido"
            val rawImage = producto?.image_path ?: ""
            val imagenProducto = if(rawImage.isNotEmpty()) almacenDatos.getAppDataDir() + "/productos/" + rawImage else ""

            // E. Crear el DTO de la linea con toda la info junta
            val lineaDTO = linea.toDTO(
                productoNombre = nombreProducto,
                productoImage = imagenProducto
            )
            
            // F. Añadir linea procesada
            datosRequeridos.add(lineaDTO)
        }

        // G. Devolver una copia del DTO Base pero con las lineas nuevas completas
        return pedidoDTOBase.copy(lineas = datosRequeridos)
    }
}
