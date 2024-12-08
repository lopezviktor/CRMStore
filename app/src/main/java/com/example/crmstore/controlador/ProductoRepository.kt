package com.example.crmstore.controlador

import com.example.crmstore.modelo.Producto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class ProductoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val productosCollection = db.collection("productos")

    // Obtener productos
    fun obtenerProductos(callback: (List<Producto>) -> Unit) {
        productosCollection.get()
            .addOnSuccessListener { snapshot ->
                val productos = snapshot.documents.mapNotNull { it.toObject<Producto>()?.apply { id = it.id } }
                callback(productos)
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                callback(emptyList())
            }
    }

    // Agregar producto
    fun agregarProducto(producto: Producto) {
        val productoData = mapOf(
            "nombre" to producto.nombre,
            "precio" to producto.precio,
            "descripcion" to producto.descripcion
        )
        productosCollection.add(producto)
            .addOnSuccessListener { documentRef ->
                documentRef.update("id", documentRef.id)
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }

    // Actualizar producto
    fun actualizarProducto(producto: Producto) {
        producto.id?.let {
            productosCollection.document(it).set(producto)
                .addOnFailureListener { exception ->
                    exception.printStackTrace()
                }
        }
    }

    // Eliminar producto
    fun eliminarProducto(id: String) {
        productosCollection.document(id).delete()
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }
}