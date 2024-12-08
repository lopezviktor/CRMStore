package com.example.crmstore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.repository.EmpleadoRepository
import com.example.crmstore.modelo.Empleado
import com.example.crmstore.modelo.Evento
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EmpleadoViewModel : ViewModel() {

    private val empleadoRepository = EmpleadoRepository()

    // Lista de empleados en tiempo real (se espera una lista de pares con ID como clave y Empleado como valor)
    private val _empleados = MutableStateFlow<List<Pair<String, Empleado>>>(emptyList())
    val empleados: StateFlow<List<Pair<String, Empleado>>> get() = _empleados

    // Lista de eventos en tiempo real
    private val _eventos = MutableStateFlow<List<Evento>>(emptyList())
    val eventos: StateFlow<List<Evento>> get() = _eventos

    init {
        // Cargar empleados y eventos en tiempo real
        cargarEmpleadosEnTiempoReal()
        cargarEventosEnTiempoReal()
    }

    // Cargar empleados en tiempo real
    private fun cargarEmpleadosEnTiempoReal() {
        empleadoRepository.obtenerEmpleados { empleadosObtenidos ->
            _empleados.value = empleadosObtenidos
        }
    }

    // Cargar eventos en tiempo real
    private fun cargarEventosEnTiempoReal() {
        empleadoRepository.obtenerEventosEnTiempoReal { eventosObtenidos ->
            _eventos.value = eventosObtenidos
        }
    }

    // Eliminar un empleado de la lista
    fun eliminarEmpleado(idDocumento: String) {
        viewModelScope.launch {
            try {
                empleadoRepository.eliminarEmpleado(idDocumento) // Elimina desde el repositorio
                _empleados.value = _empleados.value.filter { it.first != idDocumento } // Filtra la lista localmente
            } catch (e: Exception) {
                println("Error al eliminar empleado: ${e.message}")
            }
        }
    }

    // Agregar un nuevo empleado
    fun agregarEmpleado(empleado: Empleado) {
        viewModelScope.launch {
            try {
                val nuevoId = empleadoRepository.agregarEmpleado(empleado) // Agrega el empleado al repositorio
                _empleados.value = _empleados.value + (nuevoId to empleado) // Añade el nuevo empleado a la lista local
            } catch (e: Exception) {
                println("Error al agregar empleado: ${e.message}")
            }
        }
    }

    // Actualizar los datos de un empleado existente
    fun actualizarEmpleado(idDocumento: String, empleadoActualizado: Empleado) {
        viewModelScope.launch {
            try {
                empleadoRepository.actualizarEmpleado(idDocumento, empleadoActualizado) // Actualiza en el repositorio
                _empleados.value = _empleados.value.map {
                    if (it.first == idDocumento) idDocumento to empleadoActualizado else it
                } // Actualiza localmente la lista
            } catch (e: Exception) {
                println("Error al actualizar empleado: ${e.message}")
            }
        }
    }

    // Obtener un empleado por su ID
    fun obtenerEmpleadoPorId(idDocumento: String): Empleado? {
        return _empleados.value.find { it.first == idDocumento }?.second
    }

    // Métodos para agregar, eliminar o actualizar eventos

    fun agregarEvento(evento: Evento) {
        viewModelScope.launch {
            try {
                empleadoRepository.agregarEvento(evento) // Agrega evento en repositorio
                // No es necesario recargar manualmente porque los eventos están en tiempo real
            } catch (e: Exception) {
                println("Error al agregar evento: ${e.message}")
            }
        }
    }

    fun eliminarEvento(idEvento: String) {
        viewModelScope.launch {
            try {
                empleadoRepository.eliminarEvento(idEvento) // Elimina evento del repositorio
                // No es necesario actualizar localmente porque los eventos están en tiempo real
            } catch (e: Exception) {
                println("Error al eliminar evento: ${e.message}")
            }
        }
    }

    fun actualizarEvento(evento: Evento) {
        viewModelScope.launch {
            try {
                empleadoRepository.actualizarEvento(evento) // Actualiza evento en el repositorio
                // No es necesario actualizar localmente porque los eventos están en tiempo real
            } catch (e: Exception) {
                println("Error al actualizar evento: ${e.message}")
            }
        }
    }
}
