package com.example.desafiofinalcompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.desafiofinalcompose.ItemsCard.listaProductos
import com.example.desafiofinalcompose.ViewModels.CamareroViewModel
import com.example.desafiofinalcompose.ViewModels.CamareroListasViewModel
import com.example.loginfirebase_25_26.LoginViewModel
import kotlin.collections.filter
import kotlin.text.lowercase



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCliente(navController: NavHostController) {

    var expandir by remember { mutableStateOf(false) }

    val viewModelLogin: LoginViewModel = viewModel()
    val viewModelListas: CamareroListasViewModel = viewModel()
    val viewModelClient: CamareroViewModel = viewModel()

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

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text("Cliente")
                },

                actions = {

                    IconButton(
                        onClick = {
                            expandir = true
                        }
                    ) {

                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Menu"
                        )
                    }

                    DropdownMenu(

                        expanded = expandir,

                        onDismissRequest = {
                            expandir = false
                        }

                    ) {

                        DropdownMenuItem(

                            text = {
                                Text("Historial")
                            },

                            onClick = {

                                expandir = false

                                navController.navigate(
                                    Rutas.pantallaHistorial
                                )
                            }
                        )

                        DropdownMenuItem(

                            text = {
                                Text("Cerrar sesión")
                            },

                            onClick = {

                                expandir = false

                                viewModelLogin.signOut(context)

                                navController.navigate(
                                    Rutas.pantallaLogin
                                )
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
        }
    }
}