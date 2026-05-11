package com.example.desafiofinalcompose.ItemsCard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.desafiofinalcompose.Models.Comanda

@Composable
fun ItemComandaCard(comanda: Comanda) {

    val total = comanda.productos.sumOf {
        it.precio * it.cantidad
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        )
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Text("PEDIDO",color = Color(0xFFF84F19),
                fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Estado: ${comanda.estado}",
                color = Color.Yellow,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            comanda.productos.forEach {
                Text(
                    text = "${it.nombre} x${it.cantidad}",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Total: ${"%.2f".format(total)} €",
                color = Color(0xFFF84F19),
                fontSize = 18.sp
            )
        }
    }
}