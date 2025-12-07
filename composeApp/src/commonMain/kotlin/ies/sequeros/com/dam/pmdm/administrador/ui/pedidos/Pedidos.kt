package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.PedidoDTO



@Composable
fun Pedidos(

    viewModel: PedidosViewModel,
    onAddPedido: () -> Unit, // Navegacion a pantalla de crear
    onEditPedido: (PedidoDTO) -> Unit // Navegacion a pantalla de editar
) {
    val items by viewModel.items.collectAsState()
    var searchText by remember { mutableStateOf("") }

    // Filtro simple por nombre de cliente
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
        // Barra Superior (Buscador + Boton Add)
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
                modifier = Modifier.weight(1f).padding(8.dp)
            )
            Spacer(Modifier.width(8.dp))
            OutlinedButton(
                onClick = { onAddPedido() },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Pedido")
            }
        }

        // Grid de Pedidos
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 400.dp) // Tarjetas adaptables
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