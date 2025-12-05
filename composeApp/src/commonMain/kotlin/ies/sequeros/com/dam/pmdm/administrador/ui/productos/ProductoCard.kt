package ies.sequeros.com.dam.pmdm.administrador.ui.productos

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ProductoDTO
import ies.sequeros.com.dam.pmdm.commons.ui.ImagenDesdePath

import vegaburguer.composeapp.generated.resources.Res
import vegaburguer.composeapp.generated.resources.plato

@Suppress("UnrememberedMutableState")
@Composable
fun ProductoCard(
    item: ProductoDTO,
    onActivate: (item: ProductoDTO) -> Unit,
    onDeactivate: (item: ProductoDTO) -> Unit,
    onEdit: (ProductoDTO) -> Unit,
    onDelete: (item: ProductoDTO) -> Unit
) {
    val cardAlpha by animateFloatAsState(if (item.enabled) 1f else 0.5f)
    val imagePath = mutableStateOf(if (item.image_path.isNotEmpty()) item.image_path else "")
    val borderColor = if (!item.enabled) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .alpha(cardAlpha),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Imagen circular con borde
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .border(3.dp, borderColor, CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                ImagenDesdePath(imagePath, Res.drawable.plato, Modifier.fillMaxWidth()) // null o recurso por defecto
            }

            // Nombre y Descripción
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${item.price} €",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }

            // Estado
            AssistChip(
                onClick = {},
                label = { Text(if (item.enabled) "Disponible" else "No disponible") },
                leadingIcon = {
                    Icon(
                        if (item.enabled) Icons.Default.CheckCircle else Icons.Default.Block,
                        contentDescription = null,
                        tint = if (item.enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                },
                colors = AssistChipDefaults.assistChipColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            )

            HorizontalDivider(Modifier.fillMaxWidth(0.8f), color = MaterialTheme.colorScheme.outlineVariant)

            // Acciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Activar / Desactivar
                OutlinedIconButton(
                    onClick = { if (item.enabled) onDeactivate(item) else onActivate(item) },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = if (item.enabled) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Icon(
                        if (item.enabled) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (item.enabled) "Desactivar" else "Activar"
                    )
                }

                // Editar
                OutlinedIconButton(onClick = { onEdit(item) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }

                // Eliminar
                OutlinedIconButton(
                    onClick = { onDelete(item) },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}