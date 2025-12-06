package ies.sequeros.com.dam.pmdm.administrador.pedidos.listar

data class PedidoDTO(
    
    val id: String,
    val status: String,
    val customerName: String?,
    val totalPrice: Double,
    val dependienteId: String?,
    val lineas: List<LineaPedidoDTO> = emptyList()
)