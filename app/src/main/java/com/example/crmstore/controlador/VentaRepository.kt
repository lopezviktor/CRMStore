package com.example.crmstore.controlador

import com.example.crmstore.modelo.Cliente
import com.example.crmstore.modelo.DetalleVenta
import com.example.crmstore.modelo.Producto
import com.example.crmstore.modelo.Venta
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class VentaRepository {

    private val db = FirebaseFirestore.getInstance()

    // Colecciones en Firebase
    private val ventasCollection = db.collection("ventas")
    private val productosCollection = db.collection("productos")

    // Obtener ventas en tiempo real
    fun obtenerVentasEnTiempoReal(onComplete: (List<Pair<String, Venta>>) -> Unit) {
        val ventasCollection = FirebaseFirestore.getInstance().collection("ventas")

        ventasCollection.addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) {
                onComplete(emptyList())
                return@addSnapshotListener
            }

            val ventas = snapshot.documents.mapNotNull { doc ->
                val venta = doc.toObject(Venta::class.java)
                if (venta != null) doc.id to venta else null // Asociar el ID del documento con la venta
            }
            onComplete(ventas)
        }
    }

    // Cargar productos desde Firebase
    fun cargarProductos(onComplete: (List<Producto>, Map<String, Producto>) -> Unit) {
        productosCollection.get().addOnSuccessListener { snapshot ->
            val productosMap = mutableMapOf<String, Producto>()
            val productos = snapshot.documents.mapNotNull { doc ->
                val producto = doc.toObject(Producto::class.java)
                if (producto != null) {
                    productosMap[doc.id] = producto
                }
                producto
            }
            onComplete(productos, productosMap)
        }.addOnFailureListener {
            onComplete(emptyList(), emptyMap())
        }
    }

    // Agregar una nueva venta a Firebase
    suspend fun agregarVenta(venta: Venta): Boolean {
        return try {
            ventasCollection.add(venta).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Eliminar una venta por ID
    suspend fun eliminarVenta(id: String): Boolean {
        return try {
            ventasCollection.document(id).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Obtener detalles de un producto por ID
    suspend fun obtenerProducto(id: String): Producto? {
        return try {
            productosCollection.document(id).get().await().toObject(Producto::class.java)
        } catch (e: Exception) {
            null
        }
    }

    // Obtener nombre del cliente por su ID (ejemplo, si hay clientes en otra colección)
    suspend fun obtenerCliente(clienteId: String): Cliente? {
        return try {
            db.collection("clientes").document(clienteId).get().await().toObject(Cliente::class.java)
        } catch (e: Exception) {
            null
        }
    }
}