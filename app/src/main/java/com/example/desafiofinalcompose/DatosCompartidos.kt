package com.example.desafiofinalcompose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import com.example.desafiofinalcompose.Models.Usuario

object DatosCompartidos {
    var usuario by mutableStateOf<Usuario?>(null)

    //var productoSeleccionado: Producto? = null
    var usuarioEditado: Usuario? = null
    //var comandaSeleccionada: Comanda? = null
    var lat: Double? = null
    var lng: Double? = null
}