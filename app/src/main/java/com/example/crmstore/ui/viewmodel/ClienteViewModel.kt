package com.example.crmstore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crmstore.controlador.ClienteRepository
import com.example.crmstore.modelo.Cliente
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Clase ViewModel para gestionar los datos de clientes y comunicar la capa de datos con la interfaz de usuario.
class ClienteViewModel : ViewModel() {

    // Instancia del repositorio, que se encarga de las operaciones directas con Firebase.
    private val clienteRepository = ClienteRepository()

    // MutableStateFlow para almacenar la lista de clientes en tiempo real.
    // Cada cliente está representado como un par (idDocumento, Cliente).
    // _clientes es privado porque solo debe ser modificado dentro del ViewModel.
    private val _clientes = MutableStateFlow<List<Pair<String, Cliente>>>(emptyList())

    // StateFlow público para exponer los datos a la interfaz de usuario de manera reactiva.
    // StateFlow se utiliza para que la IU pueda observar los cambios en los datos.
    val clientes: StateFlow<List<Pair<String, Cliente>>> get() = _clientes

    // Bloque de inicialización: al crear una instancia del ViewModel, se cargan los clientes desde Firebase.
    init {
        cargarClientesEnTiempoReal()
    }

    /**
     * Carga los clientes desde Firebase en tiempo real.
     * Se suscribe a los cambios en la colección "clientes" y actualiza el StateFlow con los datos obtenidos.
     */
    private fun cargarClientesEnTiempoReal() {
        clienteRepository.obtenerClientes { clientesObtenidos ->
            _clientes.value = clientesObtenidos // Actualiza el flujo con la nueva lista de clientes.
        }
    }

    /**
     * Elimina un cliente dado su ID de documento en Firebase.
     *
     * @param idDocumento El ID del documento que representa al cliente en la base de datos.
     * Se elimina tanto de Firebase como de la lista local (_clientes).
     */
    fun eliminarCliente(idDocumento: String) {
        viewModelScope.launch { // Lanza una operación asincrónica en el alcance del ViewModel.
            try {
                // Llama al repositorio para eliminar el cliente de Firebase.
                clienteRepository.eliminarCliente(idDocumento)

                // Filtra la lista local para eliminar el cliente sin necesidad de recargar todo.
                _clientes.value = _clientes.value.filter { it.first != idDocumento }
            } catch (e: Exception) {
                // Manejo de errores: imprime un mensaje en caso de fallo.
                println("Error al eliminar cliente: ${e.message}")
            }
        }
    }
}