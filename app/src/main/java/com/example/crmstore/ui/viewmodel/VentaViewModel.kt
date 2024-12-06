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

    val clientes = mutableStateListOf<String>() // Lista de clientes desde Firebase
    val empleados = mutableStateListOf<String>() // Lista de empleados desde Firebase

    // Lista observable de ventas para la UI
    val ventas = mutableStateListOf<Pair<String, Venta>>() // El ID del documento y el objeto Venta

    // Mensaje para notificaciones en la UI
    val mensaje = mutableStateOf("")

    // Carrito de productos seleccionados
    val carrito = mutableStateListOf<DetalleVenta>()

    // Cliente y empleado seleccionados
    val clienteSeleccionado = mutableStateOf<String?>(null)
    val empleadoSeleccionado = mutableStateOf<String?>(null)

    // Mapa para asociar productos con sus IDs de Firebase
    private val productosMap = mutableMapOf<String, Producto>()

    // Lista de productos observables para la UI
    val productos = mutableStateListOf<Producto>()

    init {
        cargarClientes()
        cargarEmpleados()
        cargarVentasEnTiempoReal()
        cargarProductos()
    }

    fun cargarClientes() {
        val db = FirebaseFirestore.getInstance() // Instancia de Firestore
        val clientesCollection = db.collection("clientes") // Nombre de la colección

        clientesCollection.get().addOnSuccessListener { snapshot ->
            clientes.clear() // Limpiamos la lista antes de agregar nuevos datos
            for (document in snapshot.documents) {
                val nombre = document.getString("nombre") ?: "Nombre desconocido"
                val apellidos = document.getString("apellidos") ?: "Apellidos desconocidos"
                clientes.add("$nombre $apellidos") // Añadimos "Nombre Apellidos" a la lista
            }
        }.addOnFailureListener { e ->
            // Manejar errores de Firebase
            println("Error al cargar clientes: ${e.message}")
        }
    }

    fun cargarEmpleados() {
        val db = FirebaseFirestore.getInstance() // Instancia de Firestore
        val empleadosCollection = db.collection("empleados") // Nombre de la colección

        empleadosCollection.get().addOnSuccessListener { snapshot ->
            empleados.clear() // Limpiamos la lista antes de agregar nuevos datos
            for (document in snapshot.documents) {
                val nombre = document.getString("nombre") ?: "Nombre desconocido"
                val apellidos = document.getString("apellidos") ?: "Apellidos desconocidos"
                empleados.add("$nombre $apellidos") // Añadimos "Nombre Apellidos" a la lista
            }
        }.addOnFailureListener { e ->
            // Manejar errores de Firebase
            println("Error al cargar empleados: ${e.message}")
        }
    }

    // Función para cargar ventas en tiempo real desde Firebase
    private fun cargarVentasEnTiempoReal() {
        ventaRepository.obtenerVentasEnTiempoReal { ventasObtenidas ->
            ventas.clear()
            ventas.addAll(ventasObtenidas)
        }
    }

    // Función para cargar productos desde Firebase y mapear sus IDs
    fun cargarProductos() {
        ventaRepository.cargarProductos { productosObtenidos, map ->
            productos.clear()
            productos.addAll(productosObtenidos)
            productosMap.clear()
            productosMap.putAll(map)
        }
    }

    // Función para agregar un producto al carrito
    fun agregarProductoAlCarrito(producto: Producto, cantidad: Int) {
        val productoId = producto.nombre // O usa un identificador único si lo tienes
        val productoEnCarrito = carrito.find { it.productoId == productoId }

        if (productoEnCarrito != null) {
            // Si ya está en el carrito, actualiza la cantidad
            productoEnCarrito.cantidad += cantidad
        } else {
            // Si no está en el carrito, agrégalo
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

    // Función para eliminar un producto del carrito
    fun eliminarProductoDelCarrito(productoId: String) {
        carrito.removeAll { it.productoId == productoId }
    }

    // Función para calcular el total del carrito
    fun calcularTotalCarrito(): Double {
        return carrito.sumOf { it.cantidad * it.precioUnitario }
    }

    // Función para agregar una nueva venta
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

    // Función para eliminar una venta por el ID del documento generado por Firestore
    fun eliminarVenta(documentId: String) {
        viewModelScope.launch {
            val resultado = ventaRepository.eliminarVenta(documentId)
            if (resultado) {
                val ventasFiltradas = ventas.filter { it.first != documentId }
                ventas.clear()
                ventas.addAll(ventasFiltradas)
                mensaje.value = "Venta eliminada exitosamente"
            } else {
                mensaje.value = "Error al eliminar la venta en Firebase"
            }
        }
    }

    // Función para limpiar el formulario después de confirmar una venta
    private fun limpiarFormulario() {
        carrito.clear()
        clienteSeleccionado.value = null
        empleadoSeleccionado.value = null
    }
}