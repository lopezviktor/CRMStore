package com.example.crmstore.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crmstore.controlador.VentaRepository
import com.example.crmstore.modelo.DetalleVenta
import com.example.crmstore.modelo.Venta
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VentaViewModel : ViewModel() {
    private val ventaRepository = VentaRepository()

    // Lista de ventas observables para la UI
    val ventas = mutableStateListOf<Venta>()

    // Mensaje para notificaciones en la UI
    val mensaje = mutableStateOf("")

    init {
        cargarVentasEnTiempoReal()
    }

    // Función para cargar ventas en tiempo real
    private fun cargarVentasEnTiempoReal() {
        ventaRepository.obtenerVentasEnTiempoReal { ventasObtenidas ->
            ventas.clear()
            ventas.addAll(ventasObtenidas)
        }
    }

    // Función para agregar una nueva venta
    fun agregarVenta(venta: Venta) {
        viewModelScope.launch {
            val resultado = ventaRepository.agregarVenta(venta)
            if (resultado) {
                mensaje.value = "Venta añadida correctamente"
            } else {
                mensaje.value = "Error al añadir la venta"
            }
        }
    }

    fun eliminarVenta(id: Int) {
        viewModelScope.launch {
            val resultado = ventaRepository.eliminarVenta(id.toString())
            if (resultado) {
                val ventasFiltradas = ventas.filter { it.id != id }
                ventas.clear()
                ventas.addAll(ventasFiltradas)
                mensaje.value = "Venta eliminada exitosamente"
            } else {
                mensaje.value = "Error al eliminar venta en Firebase"
            }
        }
    }

    // Extensión para convertir String a Date
    fun String.toDate(): Date? {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            sdf.parse(this)
        } catch (e: Exception) {
            null
        }
    }

    // Función para filtrar ventas por rango de fechas
    fun filtrarVentasPorRango(ventas: List<Venta>, fechaInicio: String, fechaFin: String): List<Venta> {
        val inicio = fechaInicio.toDate()
        val fin = fechaFin.toDate()

        return if (inicio != null && fin != null) {
            ventas.filter { venta ->
                val fechaVenta = venta.fecha.toDate() // Convierte el Timestamp a Date
                fechaVenta in inicio..fin
            }
        } else {
            emptyList() // Devuelve una lista vacía si las fechas no son válidas
        }
    }

    // Función para obtener productos más vendidos
    fun obtenerProductosMasVendidos(): Map<Long, Int> {
        val conteoProductos = mutableMapOf<Long, Int>()
        ventas.forEach { venta ->
            venta.productosVendidos.forEach { detalle ->
                conteoProductos[detalle.productoId] =
                    (conteoProductos[detalle.productoId] ?: 0) + detalle.cantidad
            }
        }
        return conteoProductos.toList().sortedByDescending { (_, cantidad) -> cantidad }.toMap()
    }

    // Obtener el nombre del cliente por su ID
    suspend fun obtenerNombreCliente(clienteId: Int): String {
        val cliente = ventaRepository.obtenerCliente(clienteId.toString())
        return cliente?.nombre ?: "Cliente no encontrado"
    }

    suspend fun obtenerDetalleProductos(detalles: List<DetalleVenta>): String {
        val productos = detalles.map { detalle ->
            val producto = ventaRepository.obtenerProducto(detalle.productoId.toString())
            "${producto?.nombre ?: "Desconocido"} (x${detalle.cantidad})"
        }
        return productos.joinToString(", ")
    }
}