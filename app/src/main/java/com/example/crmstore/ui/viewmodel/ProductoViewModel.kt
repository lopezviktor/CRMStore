package com.example.crmstore.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.crmstore.controlador.ProductoRepository
import com.example.crmstore.modelo.Producto
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProductoViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val productoRepository = ProductoRepository()
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        Firebase.firestore.collection("productos")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("ProductoViewModel", "Error al cargar productos", exception)
                    return@addSnapshotListener
                }
                val productosList = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Producto::class.java)?.also {
                        it.id = doc.id // Si necesitas asignar el ID del documento
                    }
                }.orEmpty()
                _productos.value = productosList
            }
    }

    fun agregarProducto(producto: Producto) {
        productoRepository.agregarProducto(producto)
    }
    fun actualizarStock(productId: String, nuevoStock: Int) {
        val producto = productos.value.find { it.id == productId }
        producto?.let {
            val productoActualizado = it.copy(stock = nuevoStock)
            productoRepository.actualizarProducto(productoActualizado)
        }
    }

    fun eliminarProducto(id: String) {
        productoRepository.eliminarProducto(id)
    }

}