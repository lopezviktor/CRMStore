package com.example.crmstore.controlador

import android.util.Log
import com.example.crmstore.modelo.Cliente
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
        ventasCollection.addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) {
                onComplete(emptyList())
                return@addSnapshotListener
            }

            val ventas = snapshot.documents.mapNotNull { doc ->
                val venta = doc.toObject(Venta::class.java)?.apply {
                    id = doc.id // Asociar el ID del documento con la venta
                }
                if (venta != null) doc.id to venta else null
            }
            onComplete(ventas)
        }
    }

    // Cargar productos desde Firebase
    fun cargarProductos(onComplete: (List<Producto>, Map<String, Producto>) -> Unit) {
        productosCollection.get().addOnSuccessListener { snapshot ->
            val productosMap = mutableMapOf<String, Producto>()
            val productos = snapshot.documents.mapNotNull { doc ->
                val producto = doc.toObject(Producto::class.java)?.apply {
                    id = doc.id // Asociar el ID del documento con el producto
                }
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
            e.printStackTrace()
            false
        }
    }

    // Reducir stock de un producto
    suspend fun reducirStockProducto(productId: String, cantidad: Int): Boolean {
        return try {
            val productoDoc = productosCollection.document(productId)
            val producto = productoDoc.get().await().toObject(Producto::class.java)

            if (producto != null) {
                if (producto.stock >= cantidad) {
                    val nuevoStock = producto.stock - cantidad
                    productoDoc.update("stock", nuevoStock).await()
                    Log.d("VentaRepository", "Stock actualizado correctamente para producto: $productId")
                    true
                } else {
                    Log.w("VentaRepository", "Stock insuficiente para producto: $productId")
                    false // Stock insuficiente
                }
            } else {
                Log.e("VentaRepository", "Producto no encontrado: $productId")
                false // Producto no existe
            }
        } catch (e: Exception) {
            Log.e("VentaRepository", "Error al reducir el stock del producto: $productId", e)
            false
        }
    }

    // Eliminar una venta por ID
    suspend fun eliminarVenta(id: String): Boolean {
        return try {
            ventasCollection.document(id).delete().await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
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

    // Obtener cliente por ID
    suspend fun obtenerCliente(clienteId: String): Cliente? {
        return try {
            db.collection("clientes").document(clienteId).get().await().toObject(Cliente::class.java)
        } catch (e: Exception) {
            null
        }
    }
    fun obtenerClientes(callback: (List<String>) -> Unit) {
        db.collection("clientes").get()
            .addOnSuccessListener { result ->
                val clientes = result.documents.mapNotNull { it.getString("nombre") }
                callback(clientes)
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                callback(emptyList())
            }
    }

    fun obtenerEmpleados(callback: (List<String>) -> Unit) {
        db.collection("empleados").get()
            .addOnSuccessListener { result ->
                val empleados = result.documents.mapNotNull { it.getString("nombre") }
                callback(empleados)
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                callback(emptyList())
            }
    }

    fun actualizarStockPorNombre(detallesVenta: List<Pair<String, Int>>): Boolean {
        try {
            detallesVenta.forEach { (nombreProducto, cantidadVendida) ->
                Log.d("ActualizarStock", "Procesando producto: $nombreProducto con cantidad: $cantidadVendida")

                productosCollection
                    .whereEqualTo("nombre", nombreProducto) // Busca por el nombre del producto
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            val document = querySnapshot.documents[0]
                            val producto = document.toObject(Producto::class.java)

                            if (producto != null) {
                                val nuevoStock = producto.stock - cantidadVendida
                                Log.d("ActualizarStock", "Nuevo stock para producto $nombreProducto: $nuevoStock")

                                if (nuevoStock >= 0) {
                                    document.reference.update("stock", nuevoStock)
                                        .addOnSuccessListener {
                                            Log.d("ActualizarStock", "Stock actualizado correctamente para producto $nombreProducto")
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e("ActualizarStock", "Error al actualizar el stock para producto $nombreProducto", e)
                                        }
                                } else {
                                    Log.e("ActualizarStock", "Stock insuficiente para producto $nombreProducto")
                                }
                            } else {
                                Log.e("ActualizarStock", "Producto no encontrado en la consulta para $nombreProducto")
                            }
                        } else {
                            Log.e("ActualizarStock", "No se encontró documento para producto: $nombreProducto")
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("ActualizarStock", "Error al buscar el producto con nombre: $nombreProducto", e)
                    }
            }
            return true
        } catch (e: Exception) {
            Log.e("ActualizarStock", "Error general en actualizarStockPorNombre", e)
            return false
        }
    }
}