package com.example.desafiofinalcompose

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController

@Composable
fun PantallaHome(navController: NavHostController) {

    val usuario = DatosCompartidos.usuario

    LaunchedEffect(usuario) {
        Log.d("DEBUG_HOME", "Rol: ${usuario?.rol}")
        when (usuario?.rol) {
            1 -> navController.navigate(Rutas.pantallaAdmin)
            2 -> navController.navigate(Rutas.pantallaCamarero)
            3 -> navController.navigate(Rutas.pantallaBarman)
            4 -> navController.navigate(Rutas.pantallaCliente)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.5f to Color(0xFF030303),
                        0.9f to Color(0xFFF81A0C),
                        1.0f to Color(0xFFF15524)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text("Cargando...", color = Color.White)
    }
}