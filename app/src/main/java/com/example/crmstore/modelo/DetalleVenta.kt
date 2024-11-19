package com.example.crmstore.modelo

data class DetalleVenta(
    var productoId: Long = 0L,
    var cantidad: Int = 0,
    var precioUnitario: Double = 0.0
)