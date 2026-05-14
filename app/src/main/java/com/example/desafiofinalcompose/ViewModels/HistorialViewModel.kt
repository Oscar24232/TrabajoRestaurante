package com.example.desafiofinalcompose.ViewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.desafiofinalcompose.DatosCompartidos
import com.example.desafiofinalcompose.Models.Comanda
import com.example.desafiofinalcompose.Models.ProductoCantidad
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HistorialViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _comandas = MutableStateFlow<List<Comanda>>(emptyList())
    val comandas: StateFlow<List<Comanda>> = _comandas

    fun cargarComandasUsuario() {

        val userId = DatosCompartidos.usuario?.id ?: return

        db.collection("comandas")
            .whereEqualTo("idusuario", userId)
            .get()
            .addOnSuccessListener { result ->

                val lista = result.mapNotNull { doc ->
                    doc.toObject(Comanda::class.java)
                }

                _comandas.value = lista
            }
    }
}