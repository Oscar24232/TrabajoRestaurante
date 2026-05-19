package com.example.desafiofinalcompose.ViewModels

import androidx.lifecycle.ViewModel
import com.example.desafiofinalcompose.Models.Comanda
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BarmanViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _comandasPendientes = MutableStateFlow<List<Comanda>>(emptyList())
    val comandasPendientes: StateFlow<List<Comanda>> = _comandasPendientes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun cargarComandasPendientes() {
        _isLoading.value = true
        db.collection("comandas")
            .whereEqualTo("estado", "pendiente")
            .get()
            .addOnSuccessListener { result ->
                val lista = result.mapNotNull { doc ->
                    val comanda = doc.toObject(Comanda::class.java)
                    comanda.id = doc.id
                    comanda
                }
                _comandasPendientes.value = lista
                _isLoading.value = false
            }
            .addOnFailureListener {
                _errorMessage.value = "Error al cargar comandas"
                _isLoading.value = false
            }
    }

    fun marcarComoServida(
        comandaId: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        db.collection("comandas")
            .document(comandaId)
            .update("estado", "servida")
            .addOnSuccessListener {
                cargarComandasPendientes()
                onSuccess()
            }
            .addOnFailureListener {
                onError()
            }
    }
}
