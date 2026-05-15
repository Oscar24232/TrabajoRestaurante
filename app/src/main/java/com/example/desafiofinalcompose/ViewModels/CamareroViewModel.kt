package com.example.desafiofinalcompose.ViewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.desafiofinalcompose.Models.Producto
import com.example.desafiofinalcompose.Models.ProductoCantidad
import kotlin.collections.indexOfFirst

class CamareroViewModel : ViewModel() {

    var comanda = mutableStateListOf<ProductoCantidad>()

    fun añadirProducto(producto: Producto) {

        val contador = comanda.indexOfFirst { it.nombre == producto.nombre }

        if (contador != -1) {
            val actual = comanda[contador]
            comanda[contador] = actual.copy(
                cantidad = actual.cantidad + 1
            )
        } else {
            comanda.add(
                ProductoCantidad(
                    id = producto.id,
                    nombre = producto.nombre,
                    cantidad = 1,
                    precio = producto.precio
                )
            )
        }
    }

    fun quitarProducto(producto: Producto) {

        val contador = comanda.indexOfFirst { it.nombre == producto.nombre }

        if (contador != -1) {
            val actual = comanda[contador]

            if (actual.cantidad > 1) {
                comanda[contador] = actual.copy(
                    cantidad = actual.cantidad - 1
                )
            } else {
                comanda.removeAt(contador)
            }
        }
    }
}