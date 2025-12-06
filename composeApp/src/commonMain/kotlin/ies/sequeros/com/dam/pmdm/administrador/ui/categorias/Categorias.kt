package ies.sequeros.com.dam.pmdm.administrador.ui.categorias

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
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.CategoriaDTO

@Composable
fun Categorias(
    categoriasViewModel: CategoriasViewModel,
    onSelectItem: (CategoriaDTO?) -> Unit
) {
    val items by categoriasViewModel.items.collectAsState()
    var searchText by remember { mutableStateOf("") }
    
    val filteredItems = items.filter {
        if (searchText.isNotBlank()) {
            it.name.contains(searchText, ignoreCase = true)
        } else {
            true
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Barra Superior
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                shape = RoundedCornerShape(16.dp),
                placeholder = { Text("Buscar categoría...") },
                leadingIcon = { Icon(Icons.Default.Search, "Search") },
                modifier = Modifier.weight(1f).padding(8.dp)
            )
            Spacer(Modifier.width(8.dp))
            OutlinedButton(
                onClick = {
                    categoriasViewModel.setSelectedCategoria(null)
                    onSelectItem(null)
                },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Add, "Add", modifier = Modifier.size(ButtonDefaults.IconSize))
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 512.dp)
        ) {
            items(filteredItems.size) { index ->
                val item = filteredItems[index]
                CategoriaCard(
                    item = item,
                    onActivate = { categoriasViewModel.switchEnableCategoria(it.copy(enabled = true)) },
                    onDeactivate = { categoriasViewModel.switchEnableCategoria(it.copy(enabled = false)) },
                    onEdit = { onSelectItem(it) },
                    onDelete = { categoriasViewModel.delete(it) }
                )
            }
        }
    }
}
