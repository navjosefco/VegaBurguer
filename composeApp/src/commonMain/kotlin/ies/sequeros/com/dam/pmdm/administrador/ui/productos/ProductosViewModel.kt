package ies.sequeros.com.dam.pmdm.administrador.ui.productos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ProductoDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ListarProductosUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.crear.CrearProductoUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.crear.CrearProductoCommand
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.actualizar.ActualizarProductoUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.actualizar.ActualizarProductoCommand
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.borrar.BorrarProductoUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.activar.ActivarProductoUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.activar.ActivarProductoCommand
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.ListarCategoriasUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.CategoriaDTO
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import ies.sequeros.com.dam.pmdm.administrador.ui.productos.form.ProductoFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductosViewModel(

    private val productoRepositorio: IProductoRepositorio,
    private val categoriaRepositorio: ICategoriaRepositorio, // Inyeccion
    private val almacenDatos: AlmacenDatos

) : ViewModel() {

    // Instancia local de casos de uso
    private val listarProductosUseCase = ListarProductosUseCase(productoRepositorio, almacenDatos)
    private val crearProductoUseCase = CrearProductoUseCase(productoRepositorio, almacenDatos)
    private val actualizarProductoUseCase = ActualizarProductoUseCase(productoRepositorio, almacenDatos)
    private val borrarProductoUseCase = BorrarProductoUseCase(productoRepositorio, almacenDatos)
    private val activarProductoUseCase = ActivarProductoUseCase(productoRepositorio, almacenDatos)
    private val listarCategoriasUseCase = ListarCategoriasUseCase(categoriaRepositorio, almacenDatos) // UC de categorias

    private val _items = MutableStateFlow<MutableList<ProductoDTO>>(mutableListOf())
    val items: StateFlow<List<ProductoDTO>> = _items.asStateFlow()

    private val _categories = MutableStateFlow<List<CategoriaDTO>>(emptyList())
    val categories: StateFlow<List<CategoriaDTO>> = _categories.asStateFlow()

    private val _selected = MutableStateFlow<ProductoDTO?>(null)
    val selected = _selected.asStateFlow()

    init {
        viewModelScope.launch {
            val list = listarProductosUseCase.invoke()
            _items.value = list.toMutableList()

            // Cargar categorias
            _categories.value = listarCategoriasUseCase.invoke()
        }
    }

    fun setSelectedProducto(item: ProductoDTO?) {

        _selected.value = item
    }

    fun switchEnableProducto(item: ProductoDTO) {

        val command = ActivarProductoCommand(item.id, item.enabled)
        viewModelScope.launch {

            val updated = activarProductoUseCase.invoke(command)
            _items.update { current ->

                current.map { if (it.id == updated.id) updated else it }.toMutableList()
            }
        }
    }

    fun delete(item: ProductoDTO) {
        viewModelScope.launch {

            borrarProductoUseCase.invoke(item.id)
            _items.update { current ->

                current.filterNot { it.id == item.id }.toMutableList()
            }
        }
    }

    // Método save que recibe el State del formulario

    fun save(formState: ProductoFormState) {
        if (_selected.value == null) {

            add(formState)
        } else {

            update(formState)
        }
    }

    private fun add(formState: ProductoFormState) {
        val command = CrearProductoCommand(
            id = "", // Generado dentro
            categoria_id = formState.categoria_id.ifBlank { "1" },
            name = formState.name,
            description = formState.description,
            price = formState.price.toDouble(),
            image_path = formState.imagePath,
            enabled = formState.enabled
        )
        viewModelScope.launch {

            try {

                val newDto = crearProductoUseCase.invoke(command)
                _items.value = (_items.value + newDto).toMutableList()

            } catch (e: Exception) {

                throw e
            }
        }
    }

    private fun update(formState: ProductoFormState) {

        val command = ActualizarProductoCommand(

            id = selected.value!!.id,
            categoria_id = formState.categoria_id.ifBlank { "1" },
            name = formState.name,
            description = formState.description,
            price = formState.price.toDouble(),
            image_path = formState.imagePath,
            enabled = formState.enabled
        )
        viewModelScope.launch {

            val updated = actualizarProductoUseCase.invoke(command)
            _items.update { current ->

                current.map { if (it.id == updated.id) updated else it }.toMutableList()
            }
        }
    }

    fun refreshCategorias(){

        viewModelScope.launch {
            _categories.value = listarCategoriasUseCase.invoke()
        }
    }
}
