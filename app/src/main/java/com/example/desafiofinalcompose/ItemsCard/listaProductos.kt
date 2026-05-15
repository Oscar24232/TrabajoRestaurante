package com.example.desafiofinalcompose.ItemsCard

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.desafiofinalcompose.Models.Producto
import com.example.desafiofinalcompose.ViewModels.CamareroViewModel

@Composable
fun listaProductos(
    titulo: String,
    productos: List<Producto>,
    viewModelClient: CamareroViewModel
) {

    Text(
        text = titulo,
        fontSize = 20.sp,
        color = Color(0xFFF84F19),
        modifier = Modifier.padding(8.dp)
    )

    productos.forEach { producto ->

        ItemProducto(
            producto = producto,
            comanda = viewModelClient.comanda,
            onAdd = viewModelClient::añadirProducto,
            onRemove = viewModelClient::quitarProducto
        )
    }
}