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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.loginfirebase_25_26.LoginViewModel

@Composable
fun PantallaCrearUsuario(navController: NavHostController) {

    val viewModel: LoginViewModel = viewModel()
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rolSeleccionado by remember { mutableStateOf(4) }
    var expandirRol by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color.Black, Color.Black, Color(0xFFF84F19))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = "Nuevo usuario",
                fontSize = 22.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre", color = Color.White) },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Black,
                    unfocusedContainerColor = Color.Black,
                    focusedIndicatorColor = Color(0xFFF84F19),
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color(0xFFF84F19)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = Color.White) },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Black,
                    unfocusedContainerColor = Color.Black,
                    focusedIndicatorColor = Color(0xFFF84F19),
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color(0xFFF84F19)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = Color.White) },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Black,
                    unfocusedContainerColor = Color.Black,
                    focusedIndicatorColor = Color(0xFFF84F19),
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color(0xFFF84F19)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row {
                OutlinedTextField(
                    value = rolATexto(rolSeleccionado),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Rol", color = Color.White) },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Black,
                        unfocusedContainerColor = Color.Black,
                        focusedIndicatorColor = Color(0xFFF84F19),
                        unfocusedIndicatorColor = Color.Gray
                    ),
                    modifier = Modifier
                        .weight(1f)
                )

                Button(
                    onClick = { expandirRol = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF84F19),
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                ) {
                    Text("Cambiar")
                }

                DropdownMenu(
                    expanded = expandirRol,
                    onDismissRequest = { expandirRol = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Admin") },
                        onClick = { rolSeleccionado = 1; expandirRol = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Camarero") },
                        onClick = { rolSeleccionado = 2; expandirRol = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Barman") },
                        onClick = { rolSeleccionado = 3; expandirRol = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Cliente") },
                        onClick = { rolSeleccionado = 4; expandirRol = false }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (nombre.isBlank() || email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (password.length < 6) {
                        Toast.makeText(context, "Mínimo 6 caracteres en la contraseña", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    viewModel.registrarUsuarioAdmin(
                        email = email,
                        password = password,
                        nombre = nombre,
                        rol = rolSeleccionado,
                        onSuccess = {
                            Toast.makeText(context, "Usuario creado", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        },
                        onError = {
                            Toast.makeText(context, "Error al crear usuario", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF84F19),
                    contentColor = Color.Black
                )
            ) {
                Text("Crear usuario", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
