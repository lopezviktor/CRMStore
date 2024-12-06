package com.example.crmstore.modelo

data class DetalleVenta(
    val productoId: String = "",
    val nombre: String = "",
    var cantidad: Int = 0,
    val precioUnitario: Double = 0.0
)