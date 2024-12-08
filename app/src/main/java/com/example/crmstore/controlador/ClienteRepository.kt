package com.example.crmstore.controlador

import com.example.crmstore.modelo.Cliente
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ClienteRepository {

    private val db = FirebaseFirestore.getInstance()

    fun obtenerClientes(onComplete: (List<Pair<String, Cliente>>) -> Unit) {
        db.collection("clientes").addSnapshotListener { snapshot, e ->
            if (e != null) {
                println("Error al obtener los clientes: ${e.message}")
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val clientes = snapshot.documents.mapNotNull { document ->
                    val cliente = document.toObject(Cliente::class.java)
                    cliente?.let { document.id to it }
                }
                onComplete(clientes)
            }
        }
    }

    suspend fun eliminarCliente(idCliente: String) {
        try {
            db.collection("clientes").document(idCliente).delete().await()
            println("Cliente eliminado con éxito")
        } catch (e: Exception) {
            println("Error al eliminar cliente: ${e.message}")
        }
    }

    suspend fun agregarCliente(cliente: Cliente): String {
        return try {
            val docRef = db.collection("clientes").add(cliente).await()
            docRef.id
        } catch (e: Exception) {
            throw Exception("Error al agregar cliente: ${e.message}")
        }
    }

    suspend fun actualizarCliente(idDocumento: String, cliente: Cliente) {
        try {
            db.collection("clientes").document(idDocumento).set(cliente).await()
        } catch (e: Exception) {
            throw Exception("Error al actualizar cliente: ${e.message}")
        }
    }
}



/*
// Clase responsable de manejar las operaciones relacionadas con clientes en la base de datos Firebase.
class ClienteRepository {

    // Instancia de FirebaseFirestore para interactuar con la base de datos.
    private val db = FirebaseFirestore.getInstance()

    /**
     * Obtiene la lista de clientes desde Firebase en tiempo real.
     *
     * @param onComplete Callback que se ejecuta cuando se obtienen los clientes correctamente.
     * Devuelve una lista de pares (ID del documento, objeto Cliente).
     */
    fun obtenerClientes(onComplete: (List<Pair<String, Cliente>>) -> Unit) {
        // Se suscribe a los cambios en la colección "clientes".
        db.collection("clientes").addSnapshotListener { snapshot, e ->
            // Si ocurre un error, imprime el mensaje y detiene la ejecución.
            if (e != null) {
                println("Error al obtener los clientes: ${e.message}")
                return@addSnapshotListener
            }

            // Si la consulta es exitosa, convierte los documentos en objetos Cliente.
            if (snapshot != null) {
                val clientes = snapshot.documents.mapNotNull { document ->
                    val cliente = document.toObject(Cliente::class.java) // Convierte el documento a un objeto Cliente.
                    cliente?.let { document.id to it } // Empareja el ID del documento con el objeto Cliente.
                }
                // Llama al callback con la lista de clientes obtenidos.
                onComplete(clientes)
            }
        }
    }

    /**
     * Elimina un cliente de Firebase dado su ID de documento.
     *
     * @param idCliente El ID del documento del cliente que se va a eliminar.
     */
    suspend fun eliminarCliente(idCliente: String) {
        try {
            // Busca y elimina el documento con el ID proporcionado.
            db.collection("clientes").document(idCliente).delete().await()
            println("Cliente eliminado con éxito")
        } catch (e: Exception) {
            // Manejo de errores: imprime un mensaje en caso de fallo.
            println("Error al eliminar cliente: ${e.message}")
        }
    }
}
*/