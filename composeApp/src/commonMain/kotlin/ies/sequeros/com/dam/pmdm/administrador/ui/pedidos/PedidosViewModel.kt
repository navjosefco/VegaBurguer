package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.actualizar.ActualizarPedidoCommand
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.actualizar.ActualizarPedidoUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.borrar.BorrarPedidoUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.crear.CrearPedidoCommand
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.crear.CrearPedidoUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.ListarPedidosUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.PedidoDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ProductoDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido
import ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.form.PedidoFormState
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PedidosViewModel(

    private val pedidoRepositorio: IPedidoRepositorio,
    private val productoRepositorio: IProductoRepositorio,
    val almacenDatos: AlmacenDatos
): ViewModel(){

    //Instanciar los casos de uso
    private val listarPedidosUseCase: ListarPedidosUseCase
    private val crearPedidoUseCase: CrearPedidoUseCase
    private val borrarPedidoUseCase: BorrarPedidoUseCase
    private val actualizarPedidoUseCase: ActualizarPedidoUseCase

    //Deckaraamos el estado de la UI

    // - El guion bajo (_) es una convención para decir "esto es privado".
    // - MutableStateFlow: Es un contenedor que guarda un dato (estado) y avisa cuando cambia.
    // - Mutable: Significa que el ViewModel PUEDE cambiar el valor (.value = ...).
    // - Es privada para que NADIE desde fuera (la UI) pueda modificarla directamente.
    // - En este caso, guarda una lista de pedidos (PedidoDTO) y avisa cuando cambia.
    private val _items = MutableStateFlow<MutableList<PedidoDTO>>(mutableListOf())
    
    // - StateFlow: Es un contenedor que guarda un dato (estado) y avisa cuando cambia.
    // - Es inmutable (no se puede cambiar directamente).
    // - Es publica para que la UI pueda acceder a ella.
    // - En este caso, expone la lista de pedidos (PedidoDTO) que el ViewModel mantiene.
    // - StateFlow: Es la versión de SOLO LECTURA.
    // - La UI se suscribe a esto (.collectAsState) para recibir actualizaciones.
    // - .asStateFlow(): Convierte la versión mutable en inmutable.
    // - Seguridad: Garantiza que la UI solo pueda LEER, nunca escribir.
    // - Cuando _items cambia, items también cambia solo que este último es inmutable.
    val items: StateFlow<List<PedidoDTO>> = _items.asStateFlow()

    

    // - En este caso, guarda un pedido (PedidoDTO) y avisa cuando cambia pero puede ser nula .
    private val _selected = MutableStateFlow<PedidoDTO?>(null)
    val selected = _selected.asStateFlow()

    //Inicializamos los casos de uso

    init{

        listarPedidosUseCase = ListarPedidosUseCase(pedidoRepositorio, productoRepositorio, almacenDatos)
        crearPedidoUseCase = CrearPedidoUseCase(pedidoRepositorio, almacenDatos)
        borrarPedidoUseCase = BorrarPedidoUseCase(pedidoRepositorio, almacenDatos)
        actualizarPedidoUseCase = ActualizarPedidoUseCase(pedidoRepositorio, almacenDatos)



        cargarPedidos()
    }

    private fun cargarPedidos(){
        //Scope: Significa Alcance pero en este caso hace referencia a la duración de la actividad.
        //ViewModelScope: Es un scope que se mantiene mientras el ViewModel está vivo.
        //launch: Inicia una nueva coroutine en el scope.
        //Cuando el ViewModel se destruye, todas las coroutines en el scope también se cancelan.
        viewModelScope.launch {

            val lista = listarPedidosUseCase.invoke()
            _items.value.clear()
            _items.value.addAll(lista)
        }
    }

    fun setSelectedPedido(pedido: PedidoDTO?){

        _selected.value = pedido
    }

    fun delete(pedido: PedidoDTO){

        viewModelScope.launch {

            borrarPedidoUseCase.invoke(pedido.id)

            _items.update { current ->

                current.filterNot { it.id == pedido.id}.toMutableList()
            }
        }
    }

    fun add(formState: PedidoFormState){

        val command = CrearPedidoCommand(
            null, //El id del dependiente no lo asignamos nosotros
            formState.customerName,
            formState.lineas
        )

        viewModelScope.launch {
            try{

                val nuevoPedido = crearPedidoUseCase.invoke(command)
                _items.value = (_items.value + nuevoPedido) as MutableList<PedidoDTO>

            }catch (e:Exception){

                throw  e
            }
        }
    }

    fun update(formState: PedidoFormState){

        val command = ActualizarPedidoCommand(
            selected.value!!.id,
            formState.status,
            formState.customerName,
            selected.value!!.dependienteId,
            formState.lineas
        )

        viewModelScope.launch {
            try{
                val pedidoActualizado = actualizarPedidoUseCase.invoke(command)

                _items.update { current ->
                    current.map{
                        if(it.id == pedidoActualizado.id)
                            pedidoActualizado
                        else
                            it
                    } as MutableList<PedidoDTO>
                }
            }catch (e:Exception){
                throw  e
            }

        }


    }
    



}