package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.PedidoDTO
import ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.form.LineaPedidoItem

@Composable
fun PedidoCard (

    item: PedidoDTO,
    onEdit: (PedidoDTO) -> Unit,
    onDelete: (PedidoDTO) -> Unit
){
    Card(

        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
    ),

    // Borde segun estado (ejemplo: terminados en verde, pendientes en naranja)
    border = if (item.status == "PENDIENTE")
    BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary)
    else null
    ) {
        Column(

            modifier = Modifier.padding(16.dp)
        ) {
            // Cabecera: Cliente y Estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = item.customerName ?: "Cliente Anonimo",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "ID: ...${item.id.takeLast(4)}", // Mostramos solo el final del UIID
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Chip de estado
                AssistChip(
                    onClick = {},
                    label = { Text(item.status) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if(item.status == "PENDIENTE")
                            MaterialTheme.colorScheme.tertiaryContainer
                        else
                            MaterialTheme.colorScheme.secondaryContainer
                    )
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Lista de productos (usando nuestro componente compacto)
            item.lineas.forEach { linea ->
                LineaPedidoItem(linea)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Pie: Total y Acciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total: ${item.totalPrice}€",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold
                )

                Row {
                    IconButton(onClick = { onEdit(item) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = { onDelete(item) }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Borrar",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}