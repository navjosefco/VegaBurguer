package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.form

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.LineaPedidoDTO
import ies.sequeros.com.dam.pmdm.commons.ui.ImagenDesdePath
import vegaburguer.composeapp.generated.resources.Res
import vegaburguer.composeapp.generated.resources.plato

import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LineaPedidoItem(linea: LineaPedidoDTO){

    val imagePath = remember {  mutableStateOf(linea.productoImage)}

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. Imagen Pequeña (Compacta)
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            ImagenDesdePath(
                path=imagePath,
                default = Res.drawable.plato,
                modifier = Modifier.fillMaxWidth()//.clip(RoundedCornerShape(8.dp))
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // 2. Cantidad y Nombre
        Column (modifier = Modifier.weight(1f)) {
            Text(
                text = "${linea.quantity}x  ${linea.productoNombre}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "${linea.unitPrice}€ / ud.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 3. Precio Total de la linea
        Text(
            text = "${linea.total}€",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }

}



// Si no te funciona ese import, prueba: import androidx.compose.desktop.ui.tooling.preview.Preview
@Preview
@Composable
fun LineaPedidoItemPreview() {
    MaterialTheme {
        // Creamos un DTO falso solo para ver cómo queda
        LineaPedidoItem(
            linea = LineaPedidoDTO(
                id = "1",
                productoId = "p1",
                productoNombre = "Burger Deluxe",
                productoImage = "", // Sin imagen real
                quantity = 2,
                unitPrice = 8.50,
                total = 17.00
            )
        )
    }
}