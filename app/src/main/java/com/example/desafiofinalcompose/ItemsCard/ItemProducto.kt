package com.example.desafiofinalcompose.ItemsCard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.desafiofinalcompose.Models.Producto
import com.example.desafiofinalcompose.Models.ProductoCantidad

@Composable
fun ItemProducto(
    producto: Producto,
    comanda: List<ProductoCantidad>,
    onAdd: (Producto) -> Unit,
    onRemove: (Producto) -> Unit,
    habilitarBotones: Boolean
) {
    val cantidad = comanda.find { it.nombre == producto.nombre }?.cantidad ?: 0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .background(Color(0xFF1E1E1E)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (producto.foto.isNotBlank()) {
            AsyncImage(
                model = producto.foto,
                contentDescription = producto.nombre,
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = producto.nombre,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${"%.2f".format(producto.precio)} €",
                color = Color(0xFFF84F19),
                fontSize = 13.sp
            )
        }

        if (habilitarBotones) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { onRemove(producto) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF84F19),
                        contentColor = Color.Black
                    )
                ) {
                    Text("-")
                }

                Text(
                    text = "$cantidad",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Button(
                    onClick = { onAdd(producto) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF84F19),
                        contentColor = Color.Black
                    )
                ) {
                    Text("+")
                }
            }
        }
    }
}