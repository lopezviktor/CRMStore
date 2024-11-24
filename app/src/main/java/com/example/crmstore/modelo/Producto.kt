package com.example.crmstore.modelo

data class Producto (
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    val categoria: String? = null,
)
