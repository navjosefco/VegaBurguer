package ies.sequeros.com.dam.pmdm.administrador.ui.productos

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
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ProductoDTO
import ies.sequeros.com.dam.pmdm.administrador.ui.MainAdministradorViewModel

@Composable
fun Productos(
    mainAdministradorViewModel: MainAdministradorViewModel,
    productosViewModel: ProductosViewModel,
    onSelectItem: (ProductoDTO?) -> Unit
) {
    val items by productosViewModel.items.collectAsState()

    var searchText by remember { mutableStateOf("") }

    val filteredItems = items.filter {
        if (searchText.isNotBlank()) {
            it.name.contains(searchText, ignoreCase = true) || it.description.contains(searchText, ignoreCase = true)
        } else {
            true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Barra superior
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                shape = RoundedCornerShape(16.dp),
                placeholder = { Text("Buscar producto...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
            Spacer(Modifier.width(8.dp))
            OutlinedButton(
                onClick = {
                    productosViewModel.setSelectedProducto(null)
                    onSelectItem(null)
                },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 512.dp)
        ) {
            items(filteredItems.size) { index ->
                val item = filteredItems[index]
                ProductoCard(
                    item = item,
                    onActivate = {
                        val element = it.copy(enabled = true)
                        productosViewModel.switchEnableProducto(element)
                    },
                    onDeactivate = {
                        val element = it.copy(enabled = false)
                        productosViewModel.switchEnableProducto(element)
                    },
                    onEdit = {
                        onSelectItem(it)
                    },
                    onDelete = {
                        productosViewModel.delete(it)
                    }
                )
            }
        }
    }
}
