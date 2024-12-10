package com.example.crmstore.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crmstore.controlador.VentaRepository
import com.example.crmstore.modelo.Cliente
import com.example.crmstore.modelo.DetalleVenta
import com.example.crmstore.modelo.Empleado
import com.example.crmstore.modelo.Producto
import com.example.crmstore.modelo.Venta
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

class VentaViewModel : ViewModel() {

    // Repositorio
    private val ventaRepository = VentaRepository()

    // Listas observables para la UI
    val clientes = mutableStateListOf<Cliente>()
    val empleados = mutableStateListOf<Empleado>()
    val ventas = mutableStateListOf<Pair<String, Venta>>()
    val productos = mutableStateListOf<Producto>()

    // Estados observables
    val mensaje = mutableStateOf("")
    val carrito = mutableStateListOf<DetalleVenta>()
    val clienteSeleccionado = mutableStateOf<Cliente?>(null)
    val empleadoSeleccionado = mutableStateOf<Empleado?>(null)

    init {
        cargarClientes()
        cargarEmpleados()
        cargarVentasEnTiempoReal()
        cargarProductos()
    }

    // ---------------- Métodos de inicialización ----------------
    fun cargarClientes() {
        Firebase.firestore.collection("clientes")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("VentaViewModel", "Error al cargar clientes", exception)
                    return@addSnapshotListener
                }
                val clientesList = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Cliente::class.java)?.also {
                        it.id = doc.id
                    }
                }.orEmpty()
                clientes.clear()
                clientes.addAll(clientesList)
            }
    }

    fun cargarEmpleados() {
        Firebase.firestore.collection("empleados")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("VentaViewModel", "Error al cargar empleados", exception)
                    return@addSnapshotListener
                }
                val empleadosList = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Empleado::class.java)?.also {
                        it.id = doc.id
                    }
                }.orEmpty()
                empleados.clear()
                empleados.addAll(empleadosList)            }
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
            cliente = clienteSeleccionado.value!!.id, // Extrae el ID del cliente seleccionado
            empleado = empleadoSeleccionado.value!!.id,
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
                venta.second.productosVendidos
            }
                .groupBy { it.nombre }
                .mapValues { entry ->
                    entry.value.sumOf { it.cantidad }
                }
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
            .groupBy { it.nombre }
            .mapValues { entry ->
                entry.value.sumOf { it.cantidad }
            }
            .entries
            .sortedByDescending { it.value }
            .take(top)
            .map { it.key to it.value }
    }

    fun obtenerClienteQueMasHaGastado(): String {
        val gastosPorCliente = mutableMapOf<String, Double>()

        ventas.forEach { (_, venta) ->
            val clienteId = venta.cliente
            val totalVenta = venta.total
            gastosPorCliente[clienteId] = gastosPorCliente.getOrDefault(clienteId, 0.0) + totalVenta
        }

        val clienteIdMasGastador = gastosPorCliente.maxByOrNull { it.value }?.key
        return clienteIdMasGastador?.let { id ->
            val cliente = clientes.find { it.id == id }
            "${cliente?.nombre} ${cliente?.apellidos}".trim()
        } ?: "Desconocido"
    }

    fun obtenerEmpleadoQueMasHaVendido(): String {
        val ventasPorEmpleado = mutableMapOf<String, Double>()

        ventas.forEach { (_, venta) ->
            val empleadoId = venta.empleado
            val totalVenta = venta.total
            ventasPorEmpleado[empleadoId] = ventasPorEmpleado.getOrDefault(empleadoId, 0.0) + totalVenta
        }

        val empleadoIdMasVendedor = ventasPorEmpleado.maxByOrNull { it.value }?.key
        return empleadoIdMasVendedor?.let { id ->
            val empleado = empleados.find { it.id == id }
            "${empleado?.nombre} ${empleado?.apellidos}".trim()
        } ?: "Desconocido"
    }

}