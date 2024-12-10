package com.example.crmstore.modelo

data class Producto(
    var id: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val stock: Int = 0,
    val categoria: String? = null
)
