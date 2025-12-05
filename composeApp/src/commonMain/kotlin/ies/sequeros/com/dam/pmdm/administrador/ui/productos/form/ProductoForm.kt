package ies.sequeros.com.dam.pmdm.administrador.ui.productos.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.productos.ProductosViewModel
import ies.sequeros.com.dam.pmdm.commons.ui.ImagenDesdePath
import ies.sequeros.com.dam.pmdm.commons.ui.SelectorImagenComposable

import vegaburguer.composeapp.generated.resources.Res
import vegaburguer.composeapp.generated.resources.plato

@Composable
fun ProductoForm(
    productosViewModel: ProductosViewModel, // ViewModel padre
    onClose: () -> Unit,
    onConfirm: (datos: ProductoFormState) -> Unit = {},

    productoFormularioViewModel: ProductoFormViewModel = viewModel {
        ProductoFormViewModel(
            productosViewModel.selected.value, onConfirm
        )
    }
) {
    val state by productoFormularioViewModel.uiState.collectAsState()
    val formValid by productoFormularioViewModel.isFormValid.collectAsState()
    val scrollState = rememberScrollState()
    val selectedItem = productosViewModel.selected.collectAsState()

    // Control de imagen local
    val imagePath = remember { mutableStateOf(if (state.imagePath.isNotEmpty()) state.imagePath else "") }

    Surface(
        modifier = Modifier.fillMaxWidth().padding(16.dp).defaultMinSize(minHeight = 200.dp),
        tonalElevation = 4.dp,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(24.dp).verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Fastfood, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(12.dp))
                Text(
                    text = if (selectedItem.value == null) "Crear Nuevo Producto" else "Editar Producto",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            // Campos
            OutlinedTextField(
                value = state.name,
                onValueChange = { productoFormularioViewModel.onNameChange(it) },
                label = { Text("Nombre") },
                isError = state.nameError != null,
                modifier = Modifier.fillMaxWidth()
            )
            state.nameError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall) }

            OutlinedTextField(
                value = state.description,
                onValueChange = { productoFormularioViewModel.onDescriptionChange(it) },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            OutlinedTextField(
                value = state.price,
                onValueChange = { productoFormularioViewModel.onPriceChange(it) },
                label = { Text("Precio (€)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = state.priceError != null,
                modifier = Modifier.fillMaxWidth()
            )
            state.priceError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall) }

            // Imagen
            Text("Imagen:", style = MaterialTheme.typography.titleSmall)
            SelectorImagenComposable({ path ->
                productoFormularioViewModel.onImagePathChange(path)
                imagePath.value = path
            })
            if(imagePath.value.isNotEmpty()) {
                ImagenDesdePath(imagePath, Res.drawable.plato
                    , Modifier.height(150.dp).fillMaxWidth())
            }
            state.imagePathError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall) }


            // Checkbox Disponible
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = state.enabled,
                    onCheckedChange = { productoFormularioViewModel.onEnabledChange(it) }
                )
                Text("Disponible")
            }

            // Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilledTonalButton(onClick = { productoFormularioViewModel.clear() }) {
                    Icon(Icons.Default.Autorenew, null)
                }

                Button(
                    onClick = {
                        productoFormularioViewModel.submit(
                            onSuccess = { onConfirm(it) }
                        )
                    },
                    enabled = formValid
                ) {
                    Icon(Icons.Default.Save, null)
                    Text(" Guardar")
                }

                FilledTonalButton(onClick = onClose) {
                    Icon(Icons.Default.Close, null)
                }
            }
        }
    }
}