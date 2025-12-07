package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.LineaPedidoDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.PedidoDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PedidoFormViewModel(

    private val pedido: PedidoDTO?,
    onSuccess: (PedidoFormState) -> Unit
): ViewModel() {

    private val _uiState = MutableStateFlow(
        PedidoFormState(

            id = pedido?.id?: "",
            customerName = pedido?.customerName?: "",
            status = pedido?.status?: "PENDIENTE",
            lineas = pedido?.lineas?: emptyList()

        )
    )

    val uiState: StateFlow<PedidoFormState> = _uiState.asStateFlow()

    //Validacion del formulario
    // - Esta variable recorre todos los campos de uiState y evalua si estan correctos
    // - Es, por decirlo de alguna forma,una variable viva que se actualiza con cualquier cambio en uiState
    // - Devuelve true si todos los campos son correctos
    val isFormValid: StateFlow<Boolean> = uiState.map { state ->
        
        state.customerNameError == null &&
        state.lineasError == null &&
        state.customerName.isNotBlank()&&
        state.lineas.isNotEmpty()

    }.stateIn(
        // - scope: Mientras que el viewModel exista, el flujo se mantendrá vivo
        // - SharingStarted.Eagerly: Comienza a emitir valores tan pronto como se crea el flujo
        // - initialValue: Valor inicial del flujo
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )

    fun onCustomerNameChange(v: String){

        _uiState.value = _uiState.value.copy(
            customerName = v,
            customerNameError = CustomerName(v)
        )
    }


    fun addLinea(linea: LineaPedidoDTO){

        val newLineas = _uiState.value.lineas.filterNot { it == linea }

        _uiState.value = _uiState.value.copy(
            lineas = newLineas,
            lineasError = validarLinea(linea)
        )
    }

    fun removeLinea(linea: LineaPedidoDTO){

        val newLineas = _uiState.value.lineas.filterNot { it == linea }

        _uiState.value = _uiState.value.copy(
            lineas = newLineas,
            lineasError = validarLinea(linea)
        )
    }

    //Hay pocos datos que cambien, no voy a hacer doble confirmacion

    fun submit(onSuccess: (PedidoFormState) -> Unit){

        viewModelScope.launch {

            if(isFormValid.value){
                onSuccess(_uiState.value)
            }
        }
    }

    private fun validarLinea(lineas: LineaPedidoDTO): String?{

        if (lineas.quantity <= 0) return "El pedido debe tener al menos una linea de producto"
        return null
    }



    private fun CustomerName(nombre: String): String? {

        if (nombre.isBlank()) return "El nombre es obligatorio"
        if (nombre.length < 2) return "El nombre es muy corto"
        return null
    }
}