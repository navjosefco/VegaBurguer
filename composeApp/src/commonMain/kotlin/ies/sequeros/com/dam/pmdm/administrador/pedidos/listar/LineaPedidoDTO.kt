package ies.sequeros.com.dam.pmdm.administrador.pedidos.listar

data class LineaPedidoDTO(

    val id: String,
    val productoId: String,
    val productoNombre: String = "",
    val quantity: Int,
    val unitPrice: Double,
    val total: Double
)