package com.example.desafiofinalcompose.ViewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.desafiofinalcompose.Models.Producto
import com.example.desafiofinalcompose.Models.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AdminViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    var listaUsuarios = mutableStateListOf<Usuario>()
    var listaProductos = mutableStateListOf<Producto>()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun cargarUsuarios() {
        _isLoading.value = true
        db.collection("usuarios")
            .get()
            .addOnSuccessListener { result ->
                listaUsuarios.clear()
                for (doc in result) {
                    val usuario = doc.toObject(Usuario::class.java)
                    usuario.id = doc.id
                    listaUsuarios.add(usuario)
                }
                _isLoading.value = false
            }
            .addOnFailureListener {
                _errorMessage.value = "Error al cargar usuarios"
                _isLoading.value = false
            }
    }

    fun actualizarRolUsuario(
        usuarioId: String,
        nuevoRol: Int,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        db.collection("usuarios")
            .document(usuarioId)
            .update("rol", nuevoRol)
            .addOnSuccessListener {
                cargarUsuarios()
                onSuccess()
            }
            .addOnFailureListener { onError() }
    }

    fun eliminarUsuario(
        usuarioId: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        db.collection("usuarios")
            .document(usuarioId)
            .delete()
            .addOnSuccessListener {
                cargarUsuarios()
                onSuccess()
            }
            .addOnFailureListener { onError() }
    }

    fun cargarProductos() {
        _isLoading.value = true
        db.collection("productos")
            .get()
            .addOnSuccessListener { result ->
                listaProductos.clear()
                for (doc in result) {
                    val producto = doc.toObject(Producto::class.java)
                    producto.id = doc.id
                    listaProductos.add(producto)
                }
                _isLoading.value = false
            }
            .addOnFailureListener {
                _errorMessage.value = "Error al cargar productos"
                _isLoading.value = false
            }
    }

    fun eliminarProducto(
        productoId: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        db.collection("productos")
            .document(productoId)
            .delete()
            .addOnSuccessListener {
                cargarProductos()
                onSuccess()
            }
            .addOnFailureListener { onError() }
    }
}
