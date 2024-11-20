package com.example.crmstore.modelo

data class Producto (
    val id: String,
    val nombre: String,
    val decripcion: String,
    val precio: Double,
    val stock: Int,
    val categoria: String? = null,
)
