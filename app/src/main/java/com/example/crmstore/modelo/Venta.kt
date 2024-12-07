package com.example.crmstore.modelo

data class Venta(
    var id: String = "", // ID generado por Firebase
    var cliente: String = "",
    var empleado: String = "",
    var fecha: com.google.firebase.Timestamp = com.google.firebase.Timestamp.now(),
    var productosVendidos: List<DetalleVenta> = emptyList(),
    var total: Double = 0.0
)