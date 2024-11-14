package com.example.crmstore.modelo

data class Venta (
    val id: Int,
    val clienteId: Int,
    val empleadoId: Int,
    val fecha: String,
    val productosVendidos: List<DetalleVenta>,
    val total: Double
)