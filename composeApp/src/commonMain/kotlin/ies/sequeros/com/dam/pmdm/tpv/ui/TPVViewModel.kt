package ies.sequeros.com.dam.pmdm.tpv.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.modelo.LineaPedido
import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto
import ies.sequeros.com.dam.pmdm.tpv.aplicacion.pedidos.registrar.RegistrarPedidoClienteCommand
import ies.sequeros.com.dam.pmdm.tpv.aplicacion.pedidos.registrar.RegistrarPedidoClienteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio

class TPVViewModel(
    private val pedidoRepositorio: IPedidoRepositorio
) : ViewModel() {

    private val registrarPedidoClienteUseCase = RegistrarPedidoClienteUseCase(pedidoRepositorio)

    private val _uiState = MutableStateFlow(TPVState())
    val uiState: StateFlow<TPVState> = _uiState.asStateFlow()

    fun setCustomerName(name: String) {
        _uiState.update { it.copy(customerName = name) }
    }

    fun resetSession() {
        _uiState.value = TPVState()
    }

    fun addProducto(producto: Producto) {

        _uiState.update { current ->
            val itemsMutable = current.items.toMutableList()
            val index = itemsMutable.indexOfFirst { it.productoId == producto.id }

            if (index >= 0) {
                val existing = itemsMutable[index]
                itemsMutable[index] = existing.copy(quantity = existing.quantity + 1)
            } else {
                itemsMutable.add(
                    ItemCarrito(
                        productoId = producto.id,
                        productoNombre = producto.name,
                        quantity = 1,
                        unitPrice = producto.price,
                        imagen = producto.image_path
                    )
                )
            }
            current.copy(items = itemsMutable)
        }
    }

    fun removeOneProducto(producto: Producto) {
        _uiState.update { current ->
            val itemsMutable = current.items.toMutableList()
            val index = itemsMutable.indexOfFirst { it.productoId == producto.id }

            if (index >= 0) {
                val existing = itemsMutable[index]
                if (existing.quantity > 1) {
                    itemsMutable[index] = existing.copy(quantity = existing.quantity - 1)
                } else {
                    itemsMutable.removeAt(index)
                }
            }
            current.copy(items = itemsMutable)
        }
    }
    
    // Método para convertir UI items a Domain LineaPedido
    fun getLineasPedidoDominio(): List<LineaPedido> {
        // En una implementación real, aquí podría haber mapeo más complejo
        // pero necesitamos generar objetos temporales para el Command
        return _uiState.value.items.map { item ->
            LineaPedido(
                id = "", // Temporal, se generará en UseCase
                pedidoId = "", // Temporal
                productoId = item.productoId,
                quantity = item.quantity,
                unitPrice = item.unitPrice
            )
        }
    }
    
    fun confirmarPedido(onSuccess: () -> Unit = {}) {
        val currentState = _uiState.value
        if(currentState.items.isEmpty()) return
        
        val command = RegistrarPedidoClienteCommand(
            clienteNombre = currentState.customerName,
            itemsCarrito = getLineasPedidoDominio()
        )
        viewModelScope.launch {
             registrarPedidoClienteUseCase(command)
             resetSession()
             onSuccess()
        }
    }
}
