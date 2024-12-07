package com.example.crmstore.controlador

import com.example.crmstore.modelo.Empleado
import com.example.crmstore.modelo.Evento
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EmpleadoRepository {

    private val db = FirebaseFirestore.getInstance()

    // Método para obtener empleados en tiempo real
    fun obtenerEmpleados(onComplete: (List<Pair<String, Empleado>>) -> Unit) {
        db.collection("empleados").addSnapshotListener { snapshot, e ->
            if (e != null) {
                println("Error al obtener los empleados: ${e.message}")
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val empleados = snapshot.documents.mapNotNull { document ->
                    val empleado = document.toObject(Empleado::class.java)
                    empleado?.let { document.id to it } // Retorna el ID del documento y el objeto empleado
                }
                onComplete(empleados)
            }
        }
    }

    // Método para agregar un empleado
    suspend fun agregarEmpleado(empleado: Empleado): String {
        return try {
            val docRef = db.collection("empleados").add(empleado).await()
            docRef.id // Retorna el ID del nuevo documento creado
        } catch (e: Exception) {
            println("Error al agregar empleado: ${e.message}")
            throw e
        }
    }

    // Método para actualizar un empleado
    suspend fun actualizarEmpleado(idDocumento: String, empleado: Empleado) {
        try {
            db.collection("empleados").document(idDocumento).set(empleado).await()
        } catch (e: Exception) {
            println("Error al actualizar empleado: ${e.message}")
            throw e
        }
    }

    // Método para eliminar un empleado
    suspend fun eliminarEmpleado(idDocumento: String) {
        try {
            db.collection("empleados").document(idDocumento).delete().await()
        } catch (e: Exception) {
            println("Error al eliminar empleado: ${e.message}")
            throw e
        }
    }

    // Método para obtener eventos (una sola vez)
    suspend fun obtenerEventos(): List<Evento> {
        return try {
            val snapshot = db.collection("eventos").get().await()
            snapshot.toObjects(Evento::class.java) // Convierte los documentos a objetos Evento
        } catch (e: Exception) {
            println("Error al obtener eventos: ${e.message}")
            emptyList() // Retorna una lista vacía en caso de error
        }
    }

    // Método para agregar un evento
    suspend fun agregarEvento(evento: Evento): String {
        return try {
            val docRef = db.collection("eventos").add(evento).await()
            docRef.id // Retorna el ID del nuevo evento creado
        } catch (e: Exception) {
            println("Error al agregar evento: ${e.message}")
            throw e
        }
    }

    // Método para actualizar un evento
    suspend fun actualizarEvento(evento: Evento) {
        try {
            db.collection("eventos").document(evento.id).set(evento).await()
        } catch (e: Exception) {
            println("Error al actualizar evento: ${e.message}")
            throw e
        }
    }

    // Método para eliminar un evento
    suspend fun eliminarEvento(idEvento: String) {
        try {
            db.collection("eventos").document(idEvento).delete().await()
        } catch (e: Exception) {
            println("Error al eliminar evento: ${e.message}")
            throw e
        }
    }

    // Método para obtener eventos en tiempo real (con addSnapshotListener)
    fun obtenerEventosEnTiempoReal(onComplete: (List<Evento>) -> Unit) {
        db.collection("eventos").addSnapshotListener { snapshot, e ->
            if (e != null) {
                println("Error al obtener eventos: ${e.message}")
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val eventos = snapshot.documents.mapNotNull { document ->
                    document.toObject(Evento::class.java)
                }
                onComplete(eventos)
            }
        }
    }
}