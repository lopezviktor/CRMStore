package com.example.crmstore.controlador

import com.example.crmstore.modelo.Venta

class VentaRepository {
    private val ventas = mutableListOf<Venta>()

    fun obtenerVentas(): List<Venta> {
        return ventas;
    }

    fun agregarVenta(venta: Venta) {
        ventas.add(venta)
    }

    fun obtenerVentaPorId(id: Int): Venta? {
        return ventas.find { it.id == id }
    }

    fun obtenerVentasPorCliente(clienteId: Int): List<Venta> {
        return ventas.filter { it.clienteId == clienteId }
    }

    fun obtenerVentasPorFecha(fecha: String): List<Venta> {
        return ventas.filter { it.fecha == fecha }
    }

    fun calcularTotalVentas(fechaInicio: String, fechaFin: String): Double {
        return ventas.filter { it.fecha in fechaInicio..fechaFin }
            .sumOf { it.total }
    }

    fun obtenerProductosMasVendidos(): Map<Int, Int> {
        val conteoProductos = mutableMapOf<Int, Int>()
        ventas.forEach { venta ->
            venta.productosVendidos.forEach { detalle ->
                val cantidadActual = conteoProductos[detalle.productoId] ?: 0
                conteoProductos[detalle.productoId] = cantidadActual + detalle.cantidad
            }
        }
        return conteoProductos.toList().sortedByDescending { (_, cantidad) -> cantidad }.toMap()
    }

    fun calcularPromedioVentaPorCliente(clienteId: Int): Double {
        val ventasCliente = obtenerVentasPorCliente(clienteId)
        return if (ventasCliente.isNotEmpty()) {
            ventasCliente.sumOf { it.total } / ventasCliente.size
        } else {
            0.0
        }
    }
}