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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
            .background(
                Brush.verticalGradient(
                    listOf(Color.Black, Color.Black, Color(0xFFF84F19))
                )
            )
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFF84F19))
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
                        }
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
    onCambiarEstado: (String) -> Unit
) {
    val total = comanda.productos.sumOf { it.precio * it.cantidad }

    val fechaFormateada = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        .format(Date(comanda.fecha))

    val colorEstado = when (comanda.estado) {
        "servida"   -> Color.Green
        "pendiente" -> Color.Yellow
        else        -> Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "PEDIDO",
                color = Color(0xFFF84F19),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Fecha: $fechaFormateada",
                color = Color.Gray,
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

            HorizontalDivider(color = Color(0xFFF84F19))

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Total: ${"%.2f".format(total)} €",
                color = Color(0xFFF84F19),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (comanda.estado == "pendiente") {
                Button(
                    onClick = { onCambiarEstado("servida") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Marcar como servida", fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = { onCambiarEstado("pendiente") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Yellow,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Marcar como pendiente", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            IconButton(
                onClick = onEliminar,
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFF84F19))
            }
        }
    }
}
