package com.example.desafiofinalcompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.desafiofinalcompose.ItemsCard.listaProductos
import com.example.desafiofinalcompose.ViewModels.CamareroListasViewModel
import com.example.desafiofinalcompose.ViewModels.CamareroViewModel

@Composable
fun PantallaCliente(navController: NavHostController) {

    val viewModelListas: CamareroListasViewModel = viewModel()
    val viewModelClient: CamareroViewModel = viewModel()

    LaunchedEffect(Unit) {
        viewModelListas.cargarProductos()
    }

    val platos = viewModelListas.productos.filter {
        it.tipo.lowercase() == "plato"
    }

    val bebidas = viewModelListas.productos.filter {
        it.tipo.lowercase() == "bebida"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
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

        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                listaProductos(
                    titulo = "Platos",
                    productos = platos,
                    viewModelClient = viewModelClient,
                    habilitarBotones = false
                )
            }
            item {
                listaProductos(
                    titulo = "Bebidas",
                    productos = bebidas,
                    viewModelClient = viewModelClient,
                    habilitarBotones = false
                )
            }
        }
    }
}