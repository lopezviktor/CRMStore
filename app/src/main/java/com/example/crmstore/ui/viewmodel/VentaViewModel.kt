package com.example.crmstore.ui.viewmodel

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

    private val ventaRepository = VentaRepository()

    // Listas observables para la UI
    val clientes = mutableStateListOf<String>() // Lista de clientes desde Firebase
    val empleados = mutableStateListOf<String>() // Lista de empleados desde Firebase
    val ventas = mutableStateListOf<Pair<String, Venta>>() // El ID del documento y el objeto Venta
    val productos = mutableStateListOf<Producto>() // Lista de productos desde Firebase

    // Mapa para asociar productos con sus IDs de Firebase
    private val productosMap = mutableMapOf<String, Producto>()

    // Estados observables
    val mensaje = mutableStateOf("")
    val carrito = mutableStateListOf<DetalleVenta>() // Carrito de productos seleccionados
    val clienteSeleccionado = mutableStateOf<String?>(null)
    val empleadoSeleccionado = mutableStateOf<String?>(null)

    init {
        cargarClientes()
        cargarEmpleados()
        cargarVentasEnTiempoReal()
        cargarProductos()
    }

    // Métodos existentes -------------------------------------------------------------

    fun cargarClientes() {
        val db = FirebaseFirestore.getInstance()
        val clientesCollection = db.collection("clientes")

        clientesCollection.get().addOnSuccessListener { snapshot ->
            clientes.clear()
            snapshot.documents.forEach { document ->
                val nombre = document.getString("nombre") ?: "Nombre desconocido"
                val apellidos = document.getString("apellidos") ?: "Apellidos desconocidos"
                clientes.add("$nombre $apellidos")
            }
        }.addOnFailureListener { e ->
            println("Error al cargar clientes: ${e.message}")
        }
    }

    fun cargarEmpleados() {
        val db = FirebaseFirestore.getInstance()
        val empleadosCollection = db.collection("empleados")

        empleadosCollection.get().addOnSuccessListener { snapshot ->
            empleados.clear()
            snapshot.documents.forEach { document ->
                val nombre = document.getString("nombre") ?: "Nombre desconocido"
                val apellidos = document.getString("apellidos") ?: "Apellidos desconocidos"
                empleados.add("$nombre $apellidos")
            }
        }.addOnFailureListener { e ->
            println("Error al cargar empleados: ${e.message}")
        }
    }

    private fun cargarVentasEnTiempoReal() {
        ventaRepository.obtenerVentasEnTiempoReal { ventasObtenidas ->
            ventas.clear()
            ventas.addAll(ventasObtenidas)
        }
    }

    fun cargarProductos() {
        ventaRepository.cargarProductos { productosObtenidos, map ->
            productos.clear()
            productos.addAll(productosObtenidos)
            productosMap.clear()
            productosMap.putAll(map)
        }
    }

    fun agregarProductoAlCarrito(producto: Producto, cantidad: Int) {
        val productoId = producto.nombre
        val productoEnCarrito = carrito.find { it.productoId == productoId }

        if (productoEnCarrito != null) {
            productoEnCarrito.cantidad += cantidad
        } else {
            carrito.add(
                DetalleVenta(
                    productoId = productoId,
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

    private fun limpiarFormulario() {
        carrito.clear()
        clienteSeleccionado.value = null
        empleadoSeleccionado.value = null
    }

    // Métodos nuevos para el dashboard ---------------------------------------------

    fun cargarVentas() {
        val db = FirebaseFirestore.getInstance()
        db.collection("ventas")
            .get()
            .addOnSuccessListener { snapshot ->
                ventas.clear()
                snapshot.documents.forEach { document ->
                    val venta = document.toObject(Venta::class.java)
                    if (venta != null) {
                        ventas.add(Pair(document.id, venta)) // Guardar el documento ID junto con la venta
                    }
                }
            }
            .addOnFailureListener { e ->
                println("Error al cargar ventas: ${e.message}")
            }
    }

    // Total de ventas
    fun calcularTotalVentas(): Double {
        return ventas.sumOf { it.second.total }
    }

    // Producto más vendido
    fun obtenerProductoMasVendido(): String {
        val conteoProductos = mutableMapOf<String, Int>()

        ventas.forEach { (_, venta) ->
            venta.productosVendidos.forEach { producto ->
                conteoProductos[producto.nombre] = conteoProductos.getOrDefault(producto.nombre, 0) + producto.cantidad
            }
        }

        return conteoProductos.maxByOrNull { it.value }?.key ?: "Ninguno"
    }

    // Promedio de venta por cliente
    fun calcularPromedioVentaPorCliente(): Double {
        val clientesUnicos = ventas.map { it.second.cliente }.distinct()
        return if (clientesUnicos.isNotEmpty()) {
            calcularTotalVentas() / clientesUnicos.size
        } else {
            0.0
        }
    }
}