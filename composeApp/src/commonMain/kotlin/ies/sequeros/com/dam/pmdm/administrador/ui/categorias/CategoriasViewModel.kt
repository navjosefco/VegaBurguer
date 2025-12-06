package ies.sequeros.com.dam.pmdm.administrador.ui.categorias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.CategoriaDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.ListarCategoriasUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.crear.CrearCategoriaUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.crear.CrearCategoriaCommand
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.actualizar.ActualizarCategoriaUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.actualizar.ActualizarCategoriaCommand
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.borrar.BorrarCategoriaUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.activar.ActivarCategoriaUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.activar.ActivarCategoriaCommand
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import ies.sequeros.com.dam.pmdm.administrador.ui.categorias.form.CategoriaFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoriasViewModel(
    private val repositorio: ICategoriaRepositorio,
    private val almacenDatos: AlmacenDatos
) : ViewModel() {

    private val listarUC = ListarCategoriasUseCase(repositorio, almacenDatos)
    private val crearUC = CrearCategoriaUseCase(repositorio, almacenDatos)
    private val actualizarUC = ActualizarCategoriaUseCase(repositorio, almacenDatos)
    private val borrarUC = BorrarCategoriaUseCase(repositorio, almacenDatos)
    private val activarUC = ActivarCategoriaUseCase(repositorio, almacenDatos)

    private val _items = MutableStateFlow<MutableList<CategoriaDTO>>(mutableListOf())
    val items: StateFlow<List<CategoriaDTO>> = _items.asStateFlow()

    private val _selected = MutableStateFlow<CategoriaDTO?>(null)
    val selected = _selected.asStateFlow()

    init {
        viewModelScope.launch {
            _items.value = listarUC.invoke().toMutableList()
        }
    }

    fun setSelectedCategoria(item: CategoriaDTO?) { _selected.value = item }

    fun switchEnableCategoria(item: CategoriaDTO) {
        val command = ActivarCategoriaCommand(item.id, item.enabled)
        viewModelScope.launch {
            val updated = activarUC.invoke(command)
            _items.update { list -> list.map { if (it.id == updated.id) updated else it }.toMutableList() }
        }
    }

    fun delete(item: CategoriaDTO) {
        viewModelScope.launch {
            borrarUC.invoke(item.id)
            /*_items.update { list -> list.filterNot { it.id == item.id }.toMutableList() } */
            // Recargar lista para verificar borrado (evitar falso positivo si hay FK)
            _items.value = listarUC.invoke().toMutableList()
        }
    }

    fun save(formState: CategoriaFormState) {
        if (_selected.value == null) add(formState) else update(formState)
    }

    private fun add(formState: CategoriaFormState) {
        val command = CrearCategoriaCommand(
            name = formState.name,
            image_path = formState.image_path,
            enabled = formState.enabled
        )
        viewModelScope.launch {
            val newDto = crearUC.invoke(command)
            _items.value = (_items.value + newDto).toMutableList()
        }
    }

    private fun update(formState: CategoriaFormState) {
        val command = ActualizarCategoriaCommand(
            id = selected.value!!.id,
            name = formState.name,
            image_path = formState.image_path,
            enabled = formState.enabled
        )
        viewModelScope.launch {
            val updated = actualizarUC.invoke(command)
            _items.update { list -> list.map { if (it.id == updated.id) updated else it }.toMutableList() }
        }
    }
}
