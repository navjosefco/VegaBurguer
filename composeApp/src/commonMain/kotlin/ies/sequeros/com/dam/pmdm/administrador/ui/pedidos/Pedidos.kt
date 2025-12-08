package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.PedidoDTO

@Composable
fun PedidosScreen(
    viewModel: PedidosViewModel,
    onEditPedido: (PedidoDTO) -> Unit 
) {
    val items by viewModel.items.collectAsState()
    var searchText by remember { mutableStateOf("") }
    
    // Filtro local por nombre de cliente
    val filteredItems = items.filter {
        if (searchText.isNotBlank()) {
            it.customerName?.contains(searchText, ignoreCase = true) == true
        } else {
            true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Barra Superior de Búsqueda
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                shape = RoundedCornerShape(16.dp),
                placeholder = { Text("Buscar cliente...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Grid adaptable de Pedidos
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 400.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(filteredItems.size) { index ->
                val item = filteredItems[index]
                PedidoCard(
                    item = item,
                    onEdit = { onEditPedido(it) },
                    onDelete = { viewModel.delete(it) }
                )
            }
        }
    }
}