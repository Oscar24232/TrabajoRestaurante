package com.example.desafiofinalcompose.ViewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.desafiofinalcompose.Models.Producto
import com.example.desafiofinalcompose.Models.Usuario
import com.google.firebase.firestore.FirebaseFirestore

class CamareroListasViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    var productos = mutableStateListOf<Producto>()
    var listaClientes = mutableStateListOf<Usuario>()

    fun cargarProductos() {
        db.collection("productos")
            .get()
            .addOnSuccessListener { result ->
                productos.clear()
                for (doc in result) {
                    val producto = doc.toObject(Producto::class.java)
                    producto.id = doc.id
                    productos.add(producto)
                }
            }
    }

    fun cargarClientes() {
        db.collection("usuarios")
            .whereEqualTo("rol", 4)
            .get()
            .addOnSuccessListener { result ->
                listaClientes.clear()
                for (doc in result) {
                    val usuario = doc.toObject(Usuario::class.java)
                    usuario.id = doc.id
                    listaClientes.add(usuario)
                }
            }
    }
}