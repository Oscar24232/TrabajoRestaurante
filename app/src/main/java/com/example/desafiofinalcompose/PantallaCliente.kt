package com.example.desafiofinalcompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.desafiofinalcompose.Models.Producto
import com.example.desafiofinalcompose.ViewModels.CamareroListasViewModel

@Composable
fun PantallaCliente(navController: NavHostController) {

    val viewModelListas: CamareroListasViewModel = viewModel()
    var filtro by remember { mutableStateOf("todos") }

    LaunchedEffect(Unit) {
        viewModelListas.cargarProductos()
    }

    val productosFiltrados = when (filtro) {
        "platos"  -> viewModelListas.productos.filter { it.tipo.lowercase() == "plato" }
        "bebidas" -> viewModelListas.productos.filter { it.tipo.lowercase() == "bebida" }
        else      -> viewModelListas.productos.toList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color.Black, Color.Black, Color(0xFFF84F19))
                )
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Button(
                onClick = { filtro = "todos" },
                modifier = Modifier.weight(1f).padding(end = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (filtro == "todos") Color(0xFFF84F19) else Color.DarkGray,
                    contentColor = Color.Black
                )
            ) {
                Text("Todos")
            }
            Button(
                onClick = { filtro = "platos" },
                modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (filtro == "platos") Color(0xFFF84F19) else Color.DarkGray,
                    contentColor = Color.Black
                )
            ) {
                Text("Platos")
            }
            Button(
                onClick = { filtro = "bebidas" },
                modifier = Modifier.weight(1f).padding(start = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (filtro == "bebidas") Color(0xFFF84F19) else Color.DarkGray,
                    contentColor = Color.Black
                )
            ) {
                Text("Bebidas")
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(productosFiltrados) { producto ->
                ItemProductoCarta(producto = producto)
            }
        }
    }
}

@Composable
fun ItemProductoCarta(producto: Producto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (producto.foto.isNotBlank()) {
                AsyncImage(
                    model = producto.foto,
                    contentDescription = producto.nombre,
                    modifier = Modifier.size(70.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Text(
                    text = producto.nombre,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${"%.2f".format(producto.precio)} €",
                    color = Color(0xFFF84F19),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}