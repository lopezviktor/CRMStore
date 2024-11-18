package com.example.crmstore.modelo

import com.google.firebase.Timestamp

data class Venta(
    var id: Int = 0,
    var clienteId: String = "",
    var empleadoId: Int = 0,
    var fecha: Timestamp = Timestamp.now(),
    var productosVendidos: List<DetalleVenta> = emptyList(),
    var total: Double = 0.0
)