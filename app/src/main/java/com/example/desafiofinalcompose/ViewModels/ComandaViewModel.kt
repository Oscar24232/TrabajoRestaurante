package com.example.desafiofinalcompose.ViewModels

import androidx.lifecycle.ViewModel
import com.example.desafiofinalcompose.DatosCompartidos
import com.example.desafiofinalcompose.Models.Comanda
import com.example.desafiofinalcompose.Models.ProductoCantidad
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.map
import kotlin.to

class ComandaViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    fun enviarComanda(
        productos: List<ProductoCantidad>,
        lat: Double?,
        lng: Double?,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {

        val userId = DatosCompartidos.usuario?.id

        if (productos.isEmpty() || lat == null || lng == null || userId == null) {
            onError()
            return
        }

        val nuevaComanda = Comanda(
            productos = productos,
            idusuario = userId,
            latitud = lat,
            longitud = lng,
            estado = "pendiente",
            fecha = System.currentTimeMillis()
        )
        val productosFinal = productos.map {
            ProductoCantidad(
                id = it.id,
                nombre = it.nombre,
                cantidad = it.cantidad,
                precio = it.precio
            )
        }
        val productosDoc = hashMapOf(
            "id" to productosFinal.map { it.id },
            "productos" to productosFinal,
            "usuario_id" to userId,
            "fecha" to System.currentTimeMillis()
        )

        db.collection("comandaProductos")
            .add(productosDoc)

        db.collection("comandas")
            .add(nuevaComanda)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onError()
            }
    }
}