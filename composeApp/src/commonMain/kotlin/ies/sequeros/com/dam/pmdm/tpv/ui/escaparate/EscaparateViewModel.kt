package ies.sequeros.com.dam.pmdm.tpv.ui.escaparate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria
import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto
import ies.sequeros.com.dam.pmdm.tpv.aplicacion.categorias.listar.ListarCategoriasUseCase
import ies.sequeros.com.dam.pmdm.tpv.aplicacion.productos.listar.ListarProductosPorCategoriaUseCase
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio

class EscaparateViewModel(

    private val categoriaRepositorio: ICategoriaRepositorio,
    private val productoRepositorio: IProductoRepositorio,
    private val almacenDatos: AlmacenDatos

) : ViewModel() {

    private val listarCategoriasUseCase = ListarCategoriasUseCase(categoriaRepositorio)
    private val listarProductosUseCase = ListarProductosPorCategoriaUseCase(productoRepositorio)

    private val _uiState = MutableStateFlow(EscaparateState())
    val uiState: StateFlow<EscaparateState> = _uiState.asStateFlow()

    init {

        cargarCategorias()
    }

    fun refreshCategorias() {
        cargarCategorias()
    }

    private fun cargarCategorias() {

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val categorias = listarCategoriasUseCase.invoke()
            
            // Seleccionamos la primera por defecto si existe
            val firstCat = categorias.firstOrNull()
            _uiState.update { 
                it.copy(
                    categorias = categorias, 
                    selectedCategoria = firstCat
                ) 
            }
            
            // Si hay categoría seleccionada, cargamos sus productos inmediatamente
            if (firstCat != null) {

                cargarProductos(firstCat.id)
            } else {

                 _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun selectCategoria(categoria: Categoria) {

        // Solo recargamos si cambia la selección
        if (_uiState.value.selectedCategoria?.id != categoria.id) {
            _uiState.update { it.copy(selectedCategoria = categoria) }
            viewModelScope.launch {
                cargarProductos(categoria.id)
            }
        }
    }

    private suspend fun cargarProductos(categoriaId: String) {

        _uiState.update { it.copy(isLoading = true) }
        val productos = listarProductosUseCase.invoke(categoriaId).map { 
             if(it.image_path.isNotEmpty()) it.copy(image_path = almacenDatos.getAppDataDir() + "/productos/" + it.image_path) else it
        }
        _uiState.update { 
            it.copy(
                productos = productos,
                isLoading = false
            ) 
        }
    }
}
