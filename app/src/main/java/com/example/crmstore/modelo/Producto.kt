package com.example.crmstore.modelo

data class Producto (
    val id: String = "",
    val nombre: String = "",
    val decripcion: String = "",
    val precio: Double = 0.0,
    val stock: Int = 0,
    val categoria: String? = null,
)
