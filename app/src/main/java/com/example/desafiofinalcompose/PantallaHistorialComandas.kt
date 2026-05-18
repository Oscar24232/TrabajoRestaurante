package com.example.desafiofinalcompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.desafiofinalcompose.ItemsCard.ItemComandaCard
import com.example.desafiofinalcompose.ViewModels.HistorialViewModel

@Composable
fun historicoComandas(navController: NavHostController, rol: Int) {

    val viewModel: HistorialViewModel = viewModel()
    val comandas by viewModel.comandas.collectAsState()

    if (rol == 4) {
        viewModel.cargarComandasUsuario()
    } else {
        viewModel.cargarComandas()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        items(comandas) { comanda ->
            ItemComandaCard(comanda)
        }
    }
}