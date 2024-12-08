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
import com.google.firebase.firestore.FirebaseFirestore
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

    // Mapa para asociar productos con sus IDs de Firebase
    private val productosMap = mutableMapOf<String, Producto>()

    init {
        cargarClientes()
        cargarEmpleados()
        cargarVentasEnTiempoReal()
        cargarProductos()
    }

    // ---------------- Métodos de inicialización ----------------

    private fun cargarClientes() {
        val db = FirebaseFirestore.getInstance()
        db.collection("clientes")
            .get()
            .addOnSuccessListener { snapshot ->
                clientes.clear()
                snapshot.documents.forEach { document ->
                    val nombre = document.getString("nombre") ?: "Nombre desconocido"
                    val apellidos = document.getString("apellidos") ?: "Apellidos desconocidos"
                    clientes.add("$nombre $apellidos")
                }
            }
            .addOnFailureListener { e ->
                println("Error al cargar clientes: ${e.message}")
            }
    }

    private fun cargarEmpleados() {
        val db = FirebaseFirestore.getInstance()
        db.collection("empleados")
            .get()
            .addOnSuccessListener { snapshot ->
                empleados.clear()
                snapshot.documents.forEach { document ->
                    val nombre = document.getString("nombre") ?: "Nombre desconocido"
                    val apellidos = document.getString("apellidos") ?: "Apellidos desconocidos"
                    empleados.add("$nombre $apellidos")
                }
            }
            .addOnFailureListener { e ->
                println("Error al cargar empleados: ${e.message}")
            }
    }

    private fun cargarVentasEnTiempoReal() {
        ventaRepository.obtenerVentasEnTiempoReal { ventasObtenidas ->
            ventas.clear()
            ventas.addAll(ventasObtenidas)
        }
    }

    private fun cargarProductos() {
        ventaRepository.cargarProductos { productosObtenidos, map ->
            productos.clear()
            productos.addAll(productosObtenidos)
            productosMap.clear()
            productosMap.putAll(map)
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
        val nuevaVenta = Venta(
            cliente = clienteSeleccionado.value ?: "",
            empleado = empleadoSeleccionado.value ?: "",
            productosVendidos = carrito.toList(),
            total = calcularTotalCarrito()
        )

        viewModelScope.launch {
            val resultado = ventaRepository.agregarVenta(nuevaVenta)
            mensaje.value = if (resultado) "Venta añadida correctamente" else "Error al añadir la venta"
            if (resultado) limpiarFormulario()
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

    fun cargarVentas() {
        val db = FirebaseFirestore.getInstance()
        db.collection("ventas")
            .get()
            .addOnSuccessListener { snapshot ->
                ventas.clear()
                snapshot.documents.forEach { document ->
                    val venta = document.toObject(Venta::class.java)
                    if (venta != null) {
                        ventas.add(document.id to venta)
                    }
                }
            }
            .addOnFailureListener { e ->
                println("Error al cargar ventas: ${e.message}")
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