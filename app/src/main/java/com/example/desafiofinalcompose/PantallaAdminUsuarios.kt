package com.example.desafiofinalcompose

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.desafiofinalcompose.Models.Usuario
import com.example.desafiofinalcompose.ViewModels.AdminViewModel

@Composable
fun PantallaAdminUsuarios(navController: NavHostController) {

    val viewModel: AdminViewModel = viewModel()
    val listaUsuarios = viewModel.listaUsuarios
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.cargarUsuarios()
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
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

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate(Rutas.pantallaRegistro) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF84F19),
                contentColor = Color.Black
            )
        ) {
            Text("+ Añadir usuario", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFF84F19))
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(listaUsuarios) { usuario ->
                    ItemUsuarioAdmin(
                        usuario = usuario,
                        onCambiarRol = { nuevoRol ->
                            viewModel.actualizarRolUsuario(
                                usuarioId = usuario.id,
                                nuevoRol = nuevoRol,
                                onSuccess = {
                                    Toast.makeText(context, "Rol actualizado", Toast.LENGTH_SHORT).show()
                                },
                                onError = {
                                    Toast.makeText(context, "Error al cambiar rol", Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                        onEliminar = {
                            viewModel.eliminarUsuario(
                                usuarioId = usuario.id,
                                onSuccess = {
                                    Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                                },
                                onError = {
                                    Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ItemUsuarioAdmin(
    usuario: Usuario,
    onCambiarRol: (Int) -> Unit,
    onEliminar: () -> Unit
) {
    var expandirRol by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = usuario.nombre,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = usuario.email,
                    color = Color.Gray,
                    fontSize = 13.sp
                )
                Text(
                    text = "Rol: ${rolATexto(usuario.rol)}",
                    color = Color(0xFFF84F19),
                    fontSize = 13.sp
                )
            }

            Box {
                IconButton(onClick = { expandirRol = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.White)
                }
                DropdownMenu(
                    expanded = expandirRol,
                    onDismissRequest = { expandirRol = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Admin") },
                        onClick = { expandirRol = false; onCambiarRol(1) }
                    )
                    DropdownMenuItem(
                        text = { Text("Camarero") },
                        onClick = { expandirRol = false; onCambiarRol(2) }
                    )
                    DropdownMenuItem(
                        text = { Text("Barman") },
                        onClick = { expandirRol = false; onCambiarRol(3) }
                    )
                    DropdownMenuItem(
                        text = { Text("Cliente") },
                        onClick = { expandirRol = false; onCambiarRol(4) }
                    )
                }
            }

            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFF84F19))
            }
        }
    }
}

fun rolATexto(rol: Int): String {
    return when (rol) {
        1    -> "Admin"
        2    -> "Camarero"
        3    -> "Barman"
        4    -> "Cliente"
        else -> "Desconocido"
    }
}
