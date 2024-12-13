package com.example.crmstore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crmstore.controlador.ClienteRepository
import com.example.crmstore.modelo.Cliente
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClienteViewModel : ViewModel() {

    private val clienteRepository = ClienteRepository()

    // Flujo para mantener la lista de clientes
    private val _clientes = MutableStateFlow<List<Pair<String, Cliente>>>(emptyList())
    val clientes: StateFlow<List<Pair<String, Cliente>>> get() = _clientes

    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> get() = _cargando

    init {
        cargarClientesEnTiempoReal()
    }

    private fun cargarClientesEnTiempoReal() {
        clienteRepository.obtenerClientes { clientesObtenidos ->
            _clientes.value = clientesObtenidos
            _cargando.value = false  // Se cambia el estado de carga cuando se completan los datos
        }
    }

    // Elimina un cliente por su ID
    fun eliminarCliente(idDocumento: String) {
        viewModelScope.launch {
            try {
                // Verifica si el cliente existe antes de intentar eliminarlo
                val clienteExistente = _clientes.value.any { it.first == idDocumento }
                if (!clienteExistente) {
                    println("Error: El cliente con ID $idDocumento no existe.")
                    return@launch
                }

                clienteRepository.eliminarCliente(idDocumento)
                // Actualiza la lista local después de eliminar
                _clientes.value = _clientes.value.filter { it.first != idDocumento }
            } catch (e: Exception) {
                println("Error al eliminar cliente: ${e.message}")
            }
        }
    }

    fun agregarCliente(cliente: Cliente) {
        viewModelScope.launch {
            try {
                val nuevoId = clienteRepository.agregarCliente(cliente)
                // Actualiza el cliente con el nuevo ID y lo añade a la lista local
                val clienteConId = cliente.copy(id = nuevoId)
                _clientes.value = _clientes.value + (nuevoId to clienteConId)
            } catch (e: Exception) {
                println("Error al agregar cliente: ${e.message}")
            }
        }
    }

    fun actualizarCliente(idDocumento: String, clienteActualizado: Cliente) {
        viewModelScope.launch {
            try {
                // Verifica si el cliente existe antes de intentar actualizarlo
                val clienteExistente = _clientes.value.find { it.first == idDocumento }
                if (clienteExistente == null) {
                    println("Error: No se encontró cliente con ID $idDocumento. Clientes disponibles:")
                    _clientes.value.forEach { println("Cliente ID: ${it.first}, Cliente: ${it.second}") }
                    return@launch
                }

                // Actualiza el cliente en el repositorio
                clienteRepository.actualizarCliente(idDocumento, clienteActualizado)
                println("Cliente con ID $idDocumento actualizado en el repositorio.")

                // Actualiza la lista local con el cliente actualizado
                _clientes.value = _clientes.value.map {
                    if (it.first == idDocumento) idDocumento to clienteActualizado else it
                }
                println("Lista local de clientes actualizada: $_clientes")

            } catch (e: Exception) {
                println("Error al actualizar cliente: ${e.message}")
            }
        }
    }

    fun obtenerClientePorId(idCliente: String): Cliente? {
        println("Buscando cliente con ID: $idCliente")
        println("Clientes disponibles:")
        _clientes.value.forEach { println("ID: ${it.first}, Cliente: ${it.second}") }

        return _clientes.value.find { it.first == idCliente }?.second
    }
}