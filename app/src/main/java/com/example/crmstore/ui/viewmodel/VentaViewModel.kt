package com.example.crmstore.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crmstore.controlador.VentaRepository
import com.example.crmstore.modelo.DetalleVenta
import com.example.crmstore.modelo.Producto
import com.example.crmstore.modelo.Venta
import kotlinx.coroutines.launch

class VentaViewModel : ViewModel() {

    // Repositorio
    private val ventaRepository = VentaRepository()

    // Listas observables para la UI
    val clientes = mutableStateListOf<String>()
    val empleados = mutableStateListOf<String>()
    val ventas = mutableStateListOf<Pair<String, Venta>>()
    val productos = mutableStateListOf<Producto>()

    // Estados observables
    val mensaje = mutableStateOf("")
    val carrito = mutableStateListOf<DetalleVenta>()
    val clienteSeleccionado = mutableStateOf<String?>(null)
    val empleadoSeleccionado = mutableStateOf<String?>(null)

    init {
        cargarClientes()
        cargarEmpleados()
        cargarVentasEnTiempoReal()
        cargarProductos()
    }

    // ---------------- Métodos de inicialización ----------------

    private fun cargarClientes() {
        viewModelScope.launch {
            ventaRepository.obtenerClientes { listaClientes ->
                clientes.clear()
                clientes.addAll(listaClientes)
            }
        }
    }

    private fun cargarEmpleados() {
        viewModelScope.launch {
            ventaRepository.obtenerEmpleados { listaEmpleados ->
                empleados.clear()
                empleados.addAll(listaEmpleados)
            }
        }
    }

    private fun cargarVentasEnTiempoReal() {
        viewModelScope.launch {
            ventaRepository.obtenerVentasEnTiempoReal { listaVentas ->
                ventas.clear()
                ventas.addAll(listaVentas)
            }
        }
    }

    private fun cargarProductos() {
        viewModelScope.launch {
            ventaRepository.cargarProductos { listaProductos, _ ->
                productos.clear()
                productos.addAll(listaProductos)
            }
        }
    }

    // ---------------- Métodos de Carrito ----------------

    fun agregarProductoAlCarrito(producto: Producto, cantidad: Int) {
        val productoEnCarrito = carrito.find { it.productoId == producto.nombre }
        if (productoEnCarrito != null) {
            productoEnCarrito.cantidad += cantidad
        } else {
            carrito.add(
                DetalleVenta(
                    productoId = producto.nombre,
                    nombre = producto.nombre,
                    cantidad = cantidad,
                    precioUnitario = producto.precio
                )
            )
        }
    }

    fun eliminarProductoDelCarrito(productoId: String) {
        carrito.removeAll { it.productoId == productoId }
    }

    fun calcularTotalCarrito(): Double {
        return carrito.sumOf { it.cantidad * it.precioUnitario }
    }

    private fun limpiarFormulario() {
        carrito.clear()
        clienteSeleccionado.value = null
        empleadoSeleccionado.value = null
    }

    // ---------------- Métodos de Ventas ----------------

    fun agregarVenta() {
        if (clienteSeleccionado.value == null || empleadoSeleccionado.value == null || carrito.isEmpty()) {
            mensaje.value = "Faltan datos para completar la venta."
            return
        }

        val nuevaVenta = Venta(
            cliente = clienteSeleccionado.value!!,
            empleado = empleadoSeleccionado.value!!,
            productosVendidos = carrito.map { it },
            total = calcularTotalCarrito()
        )

        viewModelScope.launch {
            // Cambiar a usar el nombre del producto para actualizar el stock
            val detallesVenta = carrito.map { detalle ->
                detalle.nombre to detalle.cantidad // Usar nombre en lugar de productoId
            }

            val ventaExitosa = ventaRepository.agregarVenta(nuevaVenta)
            if (ventaExitosa) {
                val stockActualizado = ventaRepository.actualizarStockPorNombre(detallesVenta)
                if (stockActualizado) {
                    limpiarFormulario()
                    mensaje.value = "Venta agregada correctamente"
                } else {
                    mensaje.value = "Error: No se pudo actualizar el stock"
                }
            } else {
                mensaje.value = "Error al agregar la venta"
            }
        }
    }

    private fun actualizarStockProductos() {
        carrito.forEach { detalle ->
            viewModelScope.launch {
                val stockReducido = ventaRepository.reducirStockProducto(detalle.productoId, detalle.cantidad)
                if (!stockReducido) {
                    mensaje.value = "Error al reducir stock para el producto: ${detalle.nombre}"
                }
            }
        }
    }

    fun eliminarVenta(documentId: String) {
        viewModelScope.launch {
            val resultado = ventaRepository.eliminarVenta(documentId)
            if (resultado) {
                ventas.removeAll { it.first == documentId }
                mensaje.value = "Venta eliminada exitosamente"
            } else {
                mensaje.value = "Error al eliminar la venta en Firebase"
            }
        }
    }

    // ---------------- Métodos para el Dashboard ----------------

    fun obtenerVentasDelMesActual(): List<Pair<String, Venta>> {
        val mesActual = com.google.firebase.Timestamp.now().toDate().month
        val anioActual = com.google.firebase.Timestamp.now().toDate().year

        return ventas.filter { (_, venta) ->
            val fechaVenta = venta.fecha.toDate()
            fechaVenta.month == mesActual && fechaVenta.year == anioActual
        }
    }

    //TOTAL
    fun calcularTotalVentas(): Double {
        return ventas.sumOf { it.second.total }
    }
    //MES ACTUAL
    fun calcularTotalVentasDelMes(): Double {
        val ventasMes = obtenerVentasDelMesActual()
        return ventasMes.sumOf { it.second.total }
    }

    // TOTAL
    fun calcularPromedioVentaPorCliente(): Double {
        val clientesUnicos = ventas.map { it.second.cliente }.distinct()
        return if (clientesUnicos.isNotEmpty()) {
            val promedio = calcularTotalVentas() / clientesUnicos.size
            String.format("%.2f", promedio).toDouble()
        } else {
            0.0
        }
    }
    //MES ACTUAL
    fun calcularPromedioVentaPorClienteDelMes(): Double {
        val ventasMes = obtenerVentasDelMesActual()
        val clientesUnicos = ventasMes.map { it.second.cliente }.distinct()
        return if (clientesUnicos.isNotEmpty()) {
            val promedio = calcularTotalVentasDelMes() / clientesUnicos.size
            String.format("%.2f", promedio).toDouble()
        } else {
            0.0
        }
    }
    //TOTAL
    fun obtenerTopProductosMasVendidos(top: Int): List<Pair<String, Int>> {
        try {
            return ventas.flatMap { venta ->
                Log.d("Debug", "Procesando venta: $venta")
                venta.second.productosVendidos // Asegúrate de que esta lista no sea nula
            }
                .groupingBy { it.nombre }
                .eachCount()
                .entries
                .sortedByDescending { it.value }
                .take(top)
                .map { it.key to it.value }
        } catch (e: Exception) {
            Log.e("Error", "Error en obtenerTopProductosMasVendidos", e)
            return emptyList()
        }
    }
    //MES ACTUAL
    fun obtenerTopProductosMasVendidosDelMes(top: Int): List<Pair<String, Int>> {
        val ventasMes = obtenerVentasDelMesActual()
        return ventasMes.flatMap { venta ->
            venta.second.productosVendidos
        }
            .groupingBy { it.nombre }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .take(top)
            .map { it.key to it.value }
    }

    fun obtenerClienteQueMasHaGastado(): String {
        val gastosPorCliente = mutableMapOf<String, Double>()

        ventas.forEach { (_, venta) ->
            val cliente = venta.cliente
            val totalVenta = venta.total
            if (cliente.isNotBlank()) {
                gastosPorCliente[cliente] = gastosPorCliente.getOrDefault(cliente, 0.0) + totalVenta
            }
        }

        return gastosPorCliente.maxByOrNull { it.value }?.key ?: "Ninguno"
    }

    fun obtenerEmpleadoQueMasHaVendido(): String {
        val ventasPorEmpleado = mutableMapOf<String, Double>()

        ventas.forEach { (_, venta) ->
            val empleado = venta.empleado
            val totalVenta = venta.total
            if (empleado.isNotBlank()) {
                ventasPorEmpleado[empleado] = ventasPorEmpleado.getOrDefault(empleado, 0.0) + totalVenta
            }
        }

        return ventasPorEmpleado.maxByOrNull { it.value }?.key ?: "Ninguno"
    }

}