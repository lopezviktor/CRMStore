package com.example.crmstore.modelo

import com.google.firebase.Timestamp

data class Venta(
    var cliente: String = "",
    var empleado: String = "",
    var fecha: Timestamp = Timestamp.now(),
    var productosVendidos: List<DetalleVenta> = emptyList(),
    var total: Double = 0.0
)