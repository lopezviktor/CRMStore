package com.example.crmstore.controlador

import com.example.crmstore.modelo.Empleado
import com.example.crmstore.modelo.Producto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProductoRepository {
    private val db = FirebaseFirestore.getInstance()

    fun obtenerProductos(onComplete: (List<Pair<String, Producto>>) -> Unit) {
        db.collection("Productos").addSnapshotListener { snapshot, e ->
            if (e != null) {
                println("Error al obtener los productos: ${e.message}")
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val productos = snapshot.documents.mapNotNull { document ->
                    val producto = document.toObject(Producto::class.java)
                    producto?.let { document.id to it }
                }
                onComplete(productos)
            }
        }
    }

    suspend fun eliminarProducto(idProducto: String) {
        try {
            db.collection("Productos").document(idProducto).delete().await()
            println("Producto eliminado con éxito")
        } catch (e: Exception) {
            println("Error al eliminar el producto: ${e.message}")
        }
    }

    suspend fun agregarProducto(producto: Producto): String {
        return try {
            val docRef = db.collection("Productos").add(producto).await()
            docRef.id
        } catch (e: Exception) {
            throw Exception("Error al agregar producto: ${e.message}")
        }
    }

    suspend fun actualizarProducto(idDocumento: String, producto: Producto) {
        try {
            db.collection("Productos").document(idDocumento).set(producto).await()
        } catch (e: Exception) {
            throw Exception("Error al actualizar empleado: ${e.message}")
        }
    }
}