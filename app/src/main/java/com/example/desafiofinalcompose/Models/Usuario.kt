package com.example.desafiofinalcompose.Models

data class Usuario(
    var id: String = "",
    var nombre: String = "",
    var email: String = "",
    var pass: String = "",
    var rol: Int=0 // 1 admin, 2 camarero, 3 barman, 4 cliente
)