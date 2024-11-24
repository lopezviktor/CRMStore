package com.example.crmstore.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.crmstore.modelo.Producto
import com.google.firebase.firestore.FirebaseFirestore


// Clase ViewModel para gestionar los datos de clientes y comunicar la capa de datos con la interfaz de usuario.
class ProductoViewModel : ViewModel() {

    // Instancia del repositorio, que se encarga de las operaciones directas con Firebas
        private val db = FirebaseFirestore.getInstance()

        fun agregarProducto(producto: Producto) {
            db.collection("productos")
                .add(producto)
                .addOnSuccessListener { documentReference ->
                    // Producto añadido exitosamente
                }
                .addOnFailureListener { e ->
                    // Manejar el error al añadir el producto
                }
        }
    }
