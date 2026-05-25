package com.example.desafiofinalcompose

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.desafiofinalcompose.Models.Comanda
import com.example.desafiofinalcompose.ViewModels.AdminComandasViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PantallaAdminComandas(navController: NavHostController) {

    val viewModel: AdminComandasViewModel = viewModel()
    val comandas by viewModel.comandas.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val context = LocalContext.current
    val usuario = DatosCompartidos.usuario

    LaunchedEffect(Unit) {
        viewModel.cargarComandas()
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        if (usuario?.rol == 1) {
            Button(
                onClick = { navController.navigate(Rutas.pantallaCamarero) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF9800),
                    contentColor = Color(0xFF0D0D0D)
                )
            ) {
                Text("+ Nueva comanda", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFFF9800))
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(comandas) { comanda ->
                    ItemComandaAdmin(
                        comanda = comanda,
                        onEliminar = {
                            viewModel.eliminarComanda(
                                comandaId = comanda.id,
                                onSuccess = {
                                    Toast.makeText(context, "Comanda eliminada", Toast.LENGTH_SHORT).show()
                                },
                                onError = {
                                    Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                        onCambiarEstado = { nuevoEstado ->
                            viewModel.cambiarEstadoComanda(
                                comandaId = comanda.id,
                                nuevoEstado = nuevoEstado,
                                onSuccess = {
                                    Toast.makeText(context, "Estado actualizado", Toast.LENGTH_SHORT).show()
                                },
                                onError = {
                                    Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                        mostrarEliminar = usuario?.rol == 1
                    )
                }
            }
        }
    }
}

@Composable
fun ItemComandaAdmin(
    comanda: Comanda,
    onEliminar: () -> Unit,
    onCambiarEstado: (String) -> Unit,
    mostrarEliminar: Boolean
) {
    val total = comanda.productos.sumOf { it.precio * it.cantidad }
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    val fechaFormateada = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        .format(Date(comanda.fecha))

    val colorEstado = when (comanda.estado) {
        "servida"   -> Color(0xFF4CAF50)
        "pendiente" -> Color(0xFFFFC107)
        else        -> Color(0xFF9E9E9E)
    }

    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            title = { Text("Eliminar comanda") },
            text = { Text("¿Estás seguro de que quieres eliminar esta comanda?") },
            confirmButton = {
                Button(
                    onClick = {
                        onEliminar()
                        mostrarDialogoEliminar = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF9800),
                        contentColor = Color(0xFF0D0D0D)
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "PEDIDO",
                color = Color(0xFFFF9800),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Fecha: $fechaFormateada",
                color = Color(0xFF9E9E9E),
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Estado: ${comanda.estado}",
                color = colorEstado,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            comanda.productos.forEach {
                Text(
                    text = "• ${it.nombre} x${it.cantidad}  —  ${"%.2f".format(it.precio * it.cantidad)} €",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(color = Color(0xFFFF9800))

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Total: ${"%.2f".format(total)} €",
                color = Color(0xFFFF9800),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (comanda.estado == "pendiente") {
                Button(
                    onClick = { onCambiarEstado("servida") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color(0xFF0D0D0D)
                    )
                ) {
                    Text("Marcar como servida", fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = { onCambiarEstado("pendiente") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFC107),
                        contentColor = Color(0xFF0D0D0D)
                    )
                ) {
                    Text("Marcar como pendiente", fontWeight = FontWeight.Bold)
                }
            }

            if (mostrarEliminar) {
                Spacer(modifier = Modifier.height(8.dp))
                IconButton(
                    onClick = { mostrarDialogoEliminar = true },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFE53935))
                }
            }
        }
    }
}