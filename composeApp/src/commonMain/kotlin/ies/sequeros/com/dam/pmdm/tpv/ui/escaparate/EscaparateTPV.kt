package ies.sequeros.com.dam.pmdm.tpv.ui.escaparate

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria
import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto
import ies.sequeros.com.dam.pmdm.tpv.ui.TPVViewModel
import ies.sequeros.com.dam.pmdm.commons.ui.ImagenDesdePath
import vegaburguer.composeapp.generated.resources.Res
import vegaburguer.composeapp.generated.resources.plato
import androidx.compose.ui.draw.clip

@Composable
fun EscaparateTPV(
    tpvViewModel: TPVViewModel,
    escaparateViewModel: EscaparateViewModel,
    onCancelarPedido: () -> Unit,
    onVerCarrito: () -> Unit
) {
    
    // Recargamos los  datos al entrar
    androidx.compose.runtime.LaunchedEffect(Unit) {
        escaparateViewModel.refreshCategorias()
    }
    
    val tpvState by tpvViewModel.uiState.collectAsState()
    val escaparateState by escaparateViewModel.uiState.collectAsState()
    
    // Estado local para mostrar el diálogo del carrito
    var showCarritoDialog by remember { mutableStateOf(false) }

    if (showCarritoDialog) {
        AlertDialog(
            onDismissRequest = { showCarritoDialog = false },
            title = { Text("Carrito de Compra") },
            text = {
                LazyColumn {
                    items(tpvState.items) { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(item.productoNombre, modifier = Modifier.weight(1f))
                            Text("x${item.quantity} (${item.total}€)", fontWeight = FontWeight.Bold)
                        }
                        HorizontalDivider()
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text("TOTAL: ${tpvState.total} €", style = MaterialTheme.typography.headlineSmall)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    showCarritoDialog = false
                    onVerCarrito() // Llama a confirmarPedido
                }) {
                    Text("CONFIRMAR Y PAGAR")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCarritoDialog = false }) {
                    Text("SEGUIR COMPRANDO")
                }
            }
        )
    }

    Column(Modifier.fillMaxSize()) {
        
        // --- HEADER ---
        Surface(
            color = MaterialTheme.colorScheme.primary, // Usar color primario del tema
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.fillMaxWidth().height(64.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hola, ${tpvState.customerName}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                ElevatedButton(
                    onClick = { showCarritoDialog = true }, // Abrir diálogo
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("${tpvState.itemCount} prod. | ${tpvState.total} €")
                }
            }
        }

        // --- BODY ---
        Row(Modifier.weight(1f)) {
            
            // PANEL IZQUIERDO: CATEGORÍAS
            LazyColumn(
                modifier = Modifier.weight(0.25f).fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surfaceVariant) // Color de tema
            ) {
                items(escaparateState.categorias) { cat ->
                    CategoriaItem(
                        categoria = cat,
                        isSelected = cat.id == escaparateState.selectedCategoria?.id,
                        onClick = { escaparateViewModel.selectCategoria(cat) }
                    )
                }
            }

            // PANEL DERECHO: PRODUCTOS
            Box(Modifier.weight(0.75f).fillMaxHeight().padding(8.dp)) {
                if (escaparateState.isLoading) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 300.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(escaparateState.productos) { producto ->
                            val itemEnCarrito = tpvState.items.find { it.productoId == producto.id }
                            
                            ProductoTPVCard(
                                producto = producto,
                                onAdd = { tpvViewModel.addProducto(producto) },
                                onRemove = { tpvViewModel.removeOneProducto(producto) },
                                cantidadEnCarrito = itemEnCarrito?.quantity ?: 0
                            )
                        }
                    }
                }
            }
        }

        // --- FOOTER ---
        Surface(
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth().height(64.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = onCancelarPedido,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("CANCELAR PEDIDO")
                }

                Button(
                    onClick = { showCarritoDialog = true }, // Abrir diálogo también aquí
                    enabled = tpvState.items.isNotEmpty()
                ) {
                    Text("VER CARRITO")
                }
            }
        }
    }
}

@Composable
fun CategoriaItem(categoria: Categoria, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent, // Tema
        contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                 val imagePath = remember(categoria.image_path) { mutableStateOf(categoria.image_path) }
                 ImagenDesdePath(imagePath, Res.drawable.plato, Modifier.fillMaxSize())
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = categoria.name,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ProductoTPVCard(
    producto: Producto,
    onAdd: () -> Unit,
    onRemove: () -> Unit,
    cantidadEnCarrito: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant) // Color solicitado
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen: Full Width, sin padding lateral
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(250.dp) // Altura fija, ancho relleno
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                 val imagePath = remember(producto.image_path) { mutableStateOf(producto.image_path) }
                 ImagenDesdePath(imagePath, Res.drawable.plato, Modifier.fillMaxSize())
            }
            
            // Contenido de texto con padding
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    producto.name, 
                    style = MaterialTheme.typography.titleMedium, 
                    fontWeight = FontWeight.Bold,
                    maxLines = 1, 
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "${producto.price} €", 
                    style = MaterialTheme.typography.bodyLarge, 
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.height(8.dp))
                
                // Botones de acción
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (cantidadEnCarrito > 0) {
                        FilledIconButton(onClick = onRemove, colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.errorContainer)) { 
                            Icon(Icons.Default.Remove, contentDescription = null, tint = MaterialTheme.colorScheme.onErrorContainer) 
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(cantidadEnCarrito.toString(), style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.width(8.dp))
                    }
                    FilledIconButton(onClick = onAdd) { 
                        Icon(Icons.Default.Add, contentDescription = null) 
                    }
                }
            }
        }
    }
}
