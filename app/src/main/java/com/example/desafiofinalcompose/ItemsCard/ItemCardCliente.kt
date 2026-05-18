package com.example.desafiofinalcompose.ItemsCard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.desafiofinalcompose.Models.Usuario

@Composable
fun ItemCardCliente(

    usuario: Usuario,

    seleccionado: Boolean,

    onClick: (Usuario) -> Unit

) {

    Card(

        modifier = Modifier
            .padding(8.dp)
            .width(170.dp)
            .clickable {
                onClick(usuario)
            },

        colors = CardDefaults.cardColors(

            containerColor = if (seleccionado)
                Color(0xFFF84F19)
            else
                Color.DarkGray
        ),

        elevation = CardDefaults.cardElevation(8.dp)

    ) {

        Column(

            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),

            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            AsyncImage(

                model = usuario,

                contentDescription = "Foto usuario",

                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),

                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(

                text = usuario.nombre,

                color = Color.White,

                fontWeight = FontWeight.Bold,

                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(

                text = usuario.email,

                color = Color.LightGray,

                fontSize = 12.sp
            )
        }
    }
}