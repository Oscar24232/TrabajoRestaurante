package com.example.desafiofinalcompose

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import com.example.desafiofinalcompose.ItemsCard.ItemProducto
import com.example.desafiofinalcompose.ItemsCard.listaProductos
import com.example.desafiofinalcompose.ViewModels.ClienteViewModel
import com.example.desafiofinalcompose.ViewModels.ClienteListasViewModel
import com.example.desafiofinalcompose.ViewModels.ComandaViewModel
import com.example.loginfirebase_25_26.LoginViewModel
import kotlin.collections.filter
import kotlin.collections.forEach
import kotlin.collections.sumOf
import kotlin.text.format
import kotlin.text.lowercase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCliente(navController: NavHostController) {

    var expandir by remember { mutableStateOf(false) }
    val viewModelLogin: LoginViewModel = viewModel()
    val viewModelClient: ClienteViewModel = viewModel()
    val viewModelListas: ClienteListasViewModel = viewModel()
    val viewModelComanda: ComandaViewModel = viewModel()
    val lat = DatosCompartidos.lat
    val lng = DatosCompartidos.lng
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModelListas.cargarProductos()
    }


    val platos = viewModelListas.productos.filter {
        it.tipo.lowercase() == "plato"
    }

    val bebidas = viewModelListas.productos.filter {
        it.tipo.lowercase() == "bebida"
    }


    val total = viewModelClient.comanda.sumOf {
        it.precio * it.cantidad
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cliente") },
                actions = {

                    IconButton(onClick = { expandir = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }

                    DropdownMenu(
                        expanded = expandir,
                        onDismissRequest = { expandir = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Cerrar sesión") },
                            onClick = {
                                expandir
                                viewModelLogin.signOut(context)
                                navController.navigate(Rutas.pantallaLogin) {
                                    //
                                }
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Historial Comandas") },
                            onClick = {
                                expandir
                                navController.navigate(Rutas.pantallaHistorial)

                            }
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black,
                            Color.Black,
                            Color(0xFFF84F19)
                        )
                    )
                )
        ) {


            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {

                item {
                    listaProductos(
                        titulo = "Platos",
                        productos = platos,
                        viewModelClient = viewModelClient
                    )
                }

                item {
                    listaProductos(
                        titulo = "Bebidas",
                        productos = bebidas,
                        viewModelClient = viewModelClient
                    )
                }
            }

            // COMANDA
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(12.dp)
            ) {

                Text(
                    "Tu pedido",
                    color = Color(0xFFF84F19),
                    fontSize = 18.sp
                )

                viewModelClient.comanda.forEach { item ->
                    Text(
                        "${item.nombre} x${item.cantidad}",
                        color = Color.White
                    )
                }

                HorizontalDivider(color = Color(0xFFF84F19))

                Text(
                    "Total: ${"%.2f".format(total)} €",
                    color = Color(0xFFF84F19),
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        navController.navigate(Rutas.pantallaMapa)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF84F19),
                        contentColor = Color.Black
                    )
                ) {
                    Text("Seleccionar ubicación")
                }

                Button(
                    onClick = {

                        viewModelComanda.enviarComanda(
                            viewModelClient.comanda.toList(),
                            onSuccess = {
                                Toast.makeText(context, "Comanda enviada", Toast.LENGTH_SHORT)
                                    .show()
                                viewModelClient.comanda.clear()
                            },
                            lat = lat,
                            lng = lng,
                            onError = {
                                Toast.makeText(context, "Error o faltan datos", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        )

                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF84F19),
                        contentColor = Color.Black
                    )
                ) {
                    Text("Enviar comanda")
                }
            }
        }
    }
}