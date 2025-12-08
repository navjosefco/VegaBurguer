package ies.sequeros.com.dam.pmdm.tpv.ui.escaparate

import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria
import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto

data class EscaparateState(

    val categorias: List<Categoria> = emptyList(),
    val productos: List<Producto> = emptyList(), 
    val selectedCategoria: Categoria? = null,
    val isLoading: Boolean = false
)
