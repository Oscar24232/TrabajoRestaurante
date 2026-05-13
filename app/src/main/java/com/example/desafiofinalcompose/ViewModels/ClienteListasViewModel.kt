package com.example.desafiofinalcompose.ViewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.desafiofinalcompose.Models.Producto
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.jvm.java

class ClienteListasViewModel: ViewModel() {
    var productos = mutableStateListOf<Producto>()

    fun cargarProductos() {

        val db = FirebaseFirestore.getInstance()

        db.collection("productos")
            .get()
            .addOnSuccessListener { result ->

                productos.clear()

                for (doc in result) {
                    val producto = doc.toObject(Producto::class.java)
                    producto.id=doc.id
                    productos.add(producto)
                }
            }
    }

}