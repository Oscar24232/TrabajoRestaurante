package com.example.desafiofinalcompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.loginfirebase_25_26.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAdmin(navController: NavHostController) {

    val usuario = DatosCompartidos.usuario
    val context = LocalContext.current
    val viewModelLogin: LoginViewModel = viewModel()

    var expanded by remember { mutableStateOf(false) }

    if (usuario?.rol != 1) {
        Text("No tienes acceso")
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin") },
                actions = {

                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {

                        DropdownMenuItem(
                            text = { Text("Añadir producto") },
                            onClick = {
                                expanded = false
                                navController.navigate(Rutas.pantallaCrearProducto)
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Cerrar sesión") },
                            onClick = {
                                expanded = false
                                viewModelLogin.signOut(context)
                                navController.navigate(Rutas.pantallaLogin)
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
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Button(onClick = {
                navController.navigate("admin_usuarios")
            }) {
                Text("Gestionar Usuarios")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                navController.navigate("admin_productos")
            }) {
                Text("Gestionar Productos")
            }
        }
    }
}