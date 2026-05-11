package com.example.desafiofinalcompose.Models

data class Producto(
    var id: String = "",
    var nombre: String = "",
    var descripcion: String = "",
    var precio: Double = 0.0,
    var foto: String = "",
    var tipo: String = ""
)