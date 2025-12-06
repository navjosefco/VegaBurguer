package ies.sequeros.com.dam.pmdm.administrador.ui.categorias.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.categorias.CategoriasViewModel
import ies.sequeros.com.dam.pmdm.commons.ui.ImagenDesdePath
import ies.sequeros.com.dam.pmdm.commons.ui.SelectorImagenComposable

import vegaburguer.composeapp.generated.resources.Res
import vegaburguer.composeapp.generated.resources.plato

@Composable
fun CategoriaForm(
    categoriasViewModel: CategoriasViewModel,
    onClose: () -> Unit,
    onConfirm: (datos: CategoriaFormState) -> Unit = {},
    categoriaFormularioViewModel: CategoriaFormViewModel = viewModel {
        CategoriaFormViewModel(categoriasViewModel.selected.value)
    }
) {
    val state by categoriaFormularioViewModel.uiState.collectAsState()
    val formValid by categoriaFormularioViewModel.isFormValid.collectAsState()
    val scrollState = rememberScrollState()
    val selectedItem = categoriasViewModel.selected.collectAsState()
    val imagePath = remember { mutableStateOf(if (state.image_path.isNotEmpty()) state.image_path else "") }

    Surface(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp).verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Category, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(12.dp))
                Text(
                    text = if (selectedItem.value == null) "Nueva Categoría" else "Editar Categoría",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            // Nombre
            OutlinedTextField(
                value = state.name,
                onValueChange = { categoriaFormularioViewModel.onNameChange(it) },
                label = { Text("Nombre") },
                isError = state.nameError != null,
                modifier = Modifier.fillMaxWidth()
            )
            state.nameError?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            // Imagen
            Text("Imagen:", style = MaterialTheme.typography.titleSmall)
            SelectorImagenComposable({ path ->
                categoriaFormularioViewModel.onImagePathChange(path)
                imagePath.value = path
            })
            if (imagePath.value.isNotEmpty()) {
                 ImagenDesdePath(imagePath, Res.drawable.plato, Modifier.height(150.dp).fillMaxWidth())
            }
            state.image_pathError?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            // Enabled
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = state.enabled,
                    onCheckedChange = { categoriaFormularioViewModel.onEnabledChange(it) }
                )
                Text("Activa")
            }

            // Buttons
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                FilledTonalButton(onClick = { categoriaFormularioViewModel.clear() }) {
                    Icon(Icons.Default.Autorenew, null)
                }
                Button(
                    onClick = { categoriaFormularioViewModel.submit(onSuccess = { onConfirm(it) }) },
                    enabled = formValid
                ) {
                    Icon(Icons.Default.Save, null); Text(" Guardar")
                }
                FilledTonalButton(onClick = onClose) {
                    Icon(Icons.Default.Close, null)
                }
            }
        }
    }
}
