package com.example.desafiofinalcompose.ViewModels

import androidx.lifecycle.ViewModel
import com.example.desafiofinalcompose.Models.Comanda
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AdminComandasViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _comandas = MutableStateFlow<List<Comanda>>(emptyList())
    val comandas: StateFlow<List<Comanda>> = _comandas

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun cargarComandas() {
        _isLoading.value = true
        db.collection("comandas")
            .get()
            .addOnSuccessListener { result ->
                val lista = result.mapNotNull { doc ->
                    val comanda = doc.toObject(Comanda::class.java)
                    comanda.id = doc.id
                    comanda
                }
                _comandas.value = lista
                _isLoading.value = false
            }
            .addOnFailureListener {
                _errorMessage.value = "Error al cargar comandas"
                _isLoading.value = false
            }
    }

    fun cambiarEstadoComanda(
        comandaId: String,
        nuevoEstado: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        db.collection("comandas")
            .document(comandaId)
            .update("estado", nuevoEstado)
            .addOnSuccessListener {
                cargarComandas()
                onSuccess()
            }
            .addOnFailureListener { onError() }
    }

    fun eliminarComanda(
        comandaId: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        db.collection("comandas")
            .document(comandaId)
            .delete()
            .addOnSuccessListener {
                cargarComandas()
                onSuccess()
            }
            .addOnFailureListener { onError() }
    }
}
