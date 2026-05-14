package com.example.desafiofinalcompose
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.desafiofinalcompose.ViewModels.ImagenViewModel
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import java.io.File
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
@Composable
fun PantallaCrearProducto(navController: NavHostController) {

    val imagenViewModel: ImagenViewModel = viewModel()
    val context = LocalContext.current

    val imageUri by imagenViewModel.imageUri.observeAsState()

    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }

    var esPlato by remember { mutableStateOf(false) }
    var esBebida by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imagenViewModel.updateImageUri(it)
            val file = File(it.path!!)
            imagenViewModel.setImageFile(file)
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Black,
                        Color.Black,
                        Color(0xFFF84F19)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text("Nuevo producto", fontSize = 22.sp, color = Color.White)


            imageUri?.let {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { launcher.launch("image/*") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF84F19),
                    contentColor = Color.White
                )
            ) {
                Text("Seleccionar imagen")
            }

            Button(
                onClick = { imagenViewModel.uploadImage(context) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF84F19),
                    contentColor = Color.White
                )
            ) {
                Text("Subir imagen")
            }

            Spacer(modifier = Modifier.height(20.dp))


            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre", color = Color.White) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,

                    focusedContainerColor = Color.Black,
                    unfocusedContainerColor = Color.Black,

                    focusedIndicatorColor = Color(0xFFF84F19),
                    unfocusedIndicatorColor = Color.Gray,

                    cursorColor = Color(0xFFF84F19)
                )
            )


            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio", color = Color.White) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,

                    focusedContainerColor = Color.Black,
                    unfocusedContainerColor = Color.Black,

                    focusedIndicatorColor = Color(0xFFF84F19),
                    unfocusedIndicatorColor = Color.Gray,

                    cursorColor = Color(0xFFF84F19)
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Checkbox(
                    checked = esPlato,
                    onCheckedChange = {
                        esPlato = it
                        if (it) esBebida = false
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFFF84F19),
                        uncheckedColor = Color.White
                    )
                )
                Text("Plato", color = Color.White)
            }

            Row {
                Checkbox(
                    checked = esBebida,
                    onCheckedChange = {
                        esBebida = it
                        if (it) esPlato = false
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFFF84F19),
                        uncheckedColor = Color.White
                    )
                )
                Text("Bebida", color = Color.White)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {

                    val tipo = if (esPlato) "plato" else "bebida"
                    val url = imagenViewModel.urlPfp.value?.toString() ?: ""

                    if (nombre.isBlank() || precio.isBlank()) {
                        Toast.makeText(context, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val precioDouble = precio.toDoubleOrNull()

                    if (precioDouble == null) {
                        Toast.makeText(context, "Precio incorrecto", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val db = FirebaseFirestore.getInstance()

                    val nuevoProducto = hashMapOf(
                        "nombre" to nombre,
                        "precio" to precioDouble,
                        "tipo" to tipo,
                        "foto" to url
                    )

                    db.collection("productos").add(nuevoProducto)

                    Toast.makeText(context, "Guardando el producto", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF84F19),
                    contentColor = Color.White
                )
            ) {
                Text("Guardar producto")
            }
        }
    }
}