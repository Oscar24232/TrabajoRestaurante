package com.example.desafiofinalcompose

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.loginfirebase_25_26.LoginViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavOptionsBuilder
import kotlin.to

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun PantallaHome(navController: NavHostController) {

    val usuario = DatosCompartidos.usuario
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val viewModelLogin: LoginViewModel = viewModel()
    LaunchedEffect(Unit) {
        Log.d("DEBUG_HOME", "Usuario en home: $usuario")
    }
    LaunchedEffect(usuario) {
        Log.d("LOG_DEBUG_ID", "${usuario?.rol}")

    }

    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text("RESTAURANTE") },

                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {

                        DropdownMenuItem(
                            text = { Text("Cerrar sesión") },
                            onClick = {
                                expanded = false
                                viewModelLogin.signOut(context)
                                navController.navigate(Rutas.pantallaLogin) {
                                    navController.popBackStack()
                                }
                            }
                        )
                    }
                }
            )
        }

    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.5f to Color(0xFF030303),
                            0.9f to Color(0xFFF81A0C),
                            1.0f to Color(0xFFF15524)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("Cargando...")
        }
    }
}