package com.example.crmstore.controlador

import com.example.crmstore.modelo.Cliente
import com.example.crmstore.modelo.Producto
import com.example.crmstore.modelo.Venta
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class VentaRepository {
    private val db = FirebaseFirestore.getInstance()

    // Obtener una venta específica por ID
    suspend fun obtenerVenta(idVenta: String): Venta? = try {
        db.collection("ventas").document(idVenta).get().await()
            .toObject(Venta::class.java)
    } catch (e: Exception) {
        println("Error al obtener venta: ${e.message}")
        null
    }

    // Obtener un cliente específico por ID
    suspend fun obtenerCliente(idCliente: String): Cliente? = try {
        db.collection("clientes").document(idCliente).get().await()
            .toObject(Cliente::class.java)
    } catch (e: Exception) {
        println("Error al obtener cliente: ${e.message}")
        null
    }

    // Obtener un producto específico por ID
    suspend fun obtenerProducto(idProducto: String): Producto? = try {
        db.collection("productos").document(idProducto).get().await()
            .toObject(Producto::class.java)
    } catch (e: Exception) {
        println("Error al obtener producto: ${e.message}")
        null
    }

    // Obtener todas las ventas en tiempo real
    fun obtenerVentasEnTiempoReal(onComplete: (List<Venta>) -> Unit) {
        db.collection("ventas").addSnapshotListener { snapshot, e ->
            if (e != null) {
                println("Error al obtener ventas en tiempo real: ${e.message}")
                return@addSnapshotListener
            }
            val ventas = snapshot?.documents?.mapNotNull { it.toObject(Venta::class.java) }.orEmpty()
            onComplete(ventas)
        }
    }

    // Agregar una nueva venta
    suspend fun agregarVenta(venta: Venta): Boolean = try {
        db.collection("ventas").add(venta).await()
        println("Venta añadida con éxito")
        true
    } catch (e: Exception) {
        println("Error al añadir venta: ${e.message}")
        false
    }

    // Eliminar una venta por ID
    suspend fun eliminarVenta(idVenta: String): Boolean = try {
        val snapshot = db.collection("ventas").whereEqualTo("id", idVenta).get().await()
        snapshot.documents.forEach { it.reference.delete().await() }
        println("Venta eliminada con éxito")
        true
    } catch (e: Exception) {
        println("Error al eliminar venta: ${e.message}")
        false
    }

    // Obtener todas las ventas por un cliente específico
    suspend fun obtenerVentasPorCliente(clienteId: String): List<Venta> = try {
        db.collection("ventas").whereEqualTo("clienteId", clienteId).get().await()
            .documents.mapNotNull { it.toObject(Venta::class.java) }
    } catch (e: Exception) {
        println("Error al obtener ventas por cliente: ${e.message}")
        emptyList()
    }

    // Calcular el total de ventas en un rango de fechas
    suspend fun calcularTotalVentasPorRango(fechaInicio: String, fechaFin: String): Double = try {
        db.collection("ventas").get().await()
            .documents
            .filter { document ->
                val fecha = document.getString("fecha") ?: ""
                fecha >= fechaInicio && fecha <= fechaFin
            }
            .sumOf { document ->
                document.getDouble("total") ?: 0.0
            }
    } catch (e: Exception) {
        println("Error al calcular total de ventas: ${e.message}")
        0.0
    }

    suspend fun obtenerProductosMasVendidos(): Map<String, Int> {
        val conteoProductos = mutableMapOf<String, Int>()
        try {
            val snapshot = db.collection("ventas").get().await()
            snapshot.documents.forEach { document ->
                val productosVendidos = document.get("productosVendidos") as? List<Map<String, Any>>
                productosVendidos?.forEach { detalle ->
                    val productoId = detalle["productoId"] as? String ?: ""
                    val cantidad = (detalle["cantidad"] as? Long)?.toInt() ?: 0
                    conteoProductos[productoId] = (conteoProductos[productoId] ?: 0) + cantidad
                }
            }
        } catch (e: Exception) {
            println("Error al obtener productos más vendidos: ${e.message}")
        }
        return conteoProductos.toList().sortedByDescending { (_, cantidad) -> cantidad }.toMap()
    }

    // Calcular el promedio de ventas por cliente
    suspend fun calcularPromedioVentasPorCliente(clienteId: String): Double {
        val ventasCliente = obtenerVentasPorCliente(clienteId)
        return if (ventasCliente.isNotEmpty()) {
            ventasCliente.sumOf { it.total } / ventasCliente.size
        } else 0.0
    }
}