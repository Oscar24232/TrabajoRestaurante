package com.example.desafiofinalcompose.ItemsCard

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import com.example.desafiofinalcompose.Models.Producto
import com.example.desafiofinalcompose.Models.ProductoCantidad
import coil.compose.AsyncImage

@Composable
fun ItemProducto(
    producto: Producto,
    comanda: List<ProductoCantidad>,
    onAdd: (Producto) -> Unit,
    onRemove: (Producto) -> Unit
) {

    val cantidad = comanda.find { it.nombre == producto.nombre }?.cantidad ?: 0
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.Gray),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AsyncImage(
            model = producto.foto,
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
        Column() {
            Spacer(modifier = Modifier.height(15.dp))
            Text(producto.nombre, color = Color.White)
            Text("${producto.precio}€", color = Color.White)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {

            Button(onClick = { onRemove(producto) }, colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF84F19),
                contentColor = Color.Black
            )) {
                Text("-")
            }

            Text("$cantidad", modifier = Modifier.padding(8.dp),color = Color.White)

            Button(onClick = { onAdd(producto) }, colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF84F19),
                contentColor = Color.Black
            )) {
                Text("+")
            }
        }
    }
}