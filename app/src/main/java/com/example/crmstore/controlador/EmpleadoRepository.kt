package com.example.app.repository

import com.example.crmstore.modelo.Empleado
import com.example.crmstore.modelo.Evento
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EmpleadoRepository {
    private val db = FirebaseFirestore.getInstance()

    // Métodos relacionados con empleados

    // Obtener empleados en tiempo real
    fun obtenerEmpleados(onComplete: (List<Pair<String, Empleado>>) -> Unit) {
        db.collection("empleados").addSnapshotListener { snapshot, e ->
            if (e != null) {
                println("Error al obtener empleados: ${e.message}")
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val empleados = snapshot.documents.mapNotNull { document ->
                    val empleado = document.toObject(Empleado::class.java)
                    empleado?.let { document.id to it } // Retorna un par (ID, Empleado)
                }
                onComplete(empleados)
            }
        }
    }

    // Agregar un empleado
    suspend fun agregarEmpleado(empleado: Empleado): String {
        return try {
            val documentRef = db.collection("empleados").add(empleado).await()
            documentRef.id // Devuelve el ID generado por Firebase
        } catch (e: Exception) {
            println("Error al agregar empleado: ${e.message}")
            throw e
        }
    }

    // Actualizar un empleado
    suspend fun actualizarEmpleado(idDocumento: String, empleadoActualizado: Empleado) {
        try {
            db.collection("empleados").document(idDocumento).set(empleadoActualizado).await()
        } catch (e: Exception) {
            println("Error al actualizar empleado: ${e.message}")
            throw e
        }
    }

    // Eliminar un empleado
    suspend fun eliminarEmpleado(idDocumento: String) {
        try {
            db.collection("empleados").document(idDocumento).delete().await()
        } catch (e: Exception) {
            println("Error al eliminar empleado: ${e.message}")
            throw e
        }
    }

    // Métodos relacionados con eventos

    // Obtener eventos en tiempo real
    fun obtenerEventosEnTiempoReal(onComplete: (List<Evento>) -> Unit) {
        db.collection("eventos").addSnapshotListener { snapshot, e ->
            if (e != null) {
                println("Error al obtener eventos: ${e.message}")
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val eventos = snapshot.documents.mapNotNull { document ->
                    val evento = document.toObject(Evento::class.java)
                    evento?.apply { id = document.id } // Asegurarse de establecer el ID
                }
                onComplete(eventos)
            }
        }
    }

    // Agregar un evento
    suspend fun agregarEvento(evento: Evento): String {
        return try {
            val documentRef = db.collection("eventos").add(evento).await()
            documentRef.id // Devuelve el ID generado por Firebase
        } catch (e: Exception) {
            println("Error al agregar evento: ${e.message}")
            throw e
        }
    }

    // Actualizar un evento
    suspend fun actualizarEvento(evento: Evento) {
        try {
            db.collection("eventos").document(evento.id).set(evento).await()
        } catch (e: Exception) {
            println("Error al actualizar evento: ${e.message}")
            throw e
        }
    }

    // Eliminar un evento
    suspend fun eliminarEvento(idEvento: String) {
        try {
            db.collection("eventos").document(idEvento).delete().await()
        } catch (e: Exception) {
            println("Error al eliminar evento: ${e.message}")
            throw e
        }
    }
}
