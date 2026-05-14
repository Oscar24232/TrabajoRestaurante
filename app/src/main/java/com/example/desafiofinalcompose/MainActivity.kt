package com.example.desafiofinalcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.borrame.ui.home.PantallaMaps
import com.example.desafiofinalcompose.ui.theme.DesafioFinalComposeTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DesafioFinalComposeTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Rutas.pantallaLogin
                ) {

                    composable(Rutas.pantallaLogin) {
                        PantallaLogin(navController)
                    }
                    composable(Rutas.pantallaRegistro) {
                        PantallaRegistro(navController)
                    }
                    composable(Rutas.pantallaHome) {
                        PantallaHome(navController)
                    }
                    composable(Rutas.pantallaAdmin) {
                        PantallaAdmin(navController)
                    }
                    composable(Rutas.pantallaCliente) {
                        PantallaCliente(navController)
                    }
                    composable(Rutas.pantallaMapa) {
                        PantallaMaps(navController)
                    }
                    composable(Rutas.pantallaCrearProducto) {
                        PantallaCrearProducto(navController)
                    }
                    composable(Rutas.pantallaHistorial) {
                        historicoComandas(navController)
                    }
                }
            }
        }
    }
}

