package com.example.desafiofinalcompose.Models

data class Comanda(
    var id: String = "",
    var productos: List<ProductoCantidad> = emptyList(),
    var estado: String = "pendiente",
    var idusuario: String ="",
    var latitud: Double = 0.0,
    var longitud: Double = 0.0,
    var fecha: Long = System.currentTimeMillis()
)