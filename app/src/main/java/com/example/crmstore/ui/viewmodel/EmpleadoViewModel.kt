package com.example.crmstore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crmstore.controlador.EmpleadoRepository
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

    // Lista de eventos
    private val _eventos = MutableStateFlow<List<Evento>>(emptyList())
    val eventos: StateFlow<List<Evento>> get() = _eventos

    init {
        // Cargar empleados y eventos
        cargarEmpleadosEnTiempoReal()
        cargarEventos()
    }

    private fun cargarEmpleadosEnTiempoReal() {
        empleadoRepository.obtenerEmpleados { empleadosObtenidos ->
            _empleados.value = empleadosObtenidos
        }
    }

    // Cargar los eventos desde el repositorio
    private fun cargarEventos() {
        viewModelScope.launch {
            try {
                _eventos.value = empleadoRepository.obtenerEventos() // Asegúrate de que esto obtenga la lista de eventos correctamente
            } catch (e: Exception) {
                println("Error al cargar eventos: ${e.message}") // Manejo básico de errores
            }
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
                val nuevoId = empleadoRepository.agregarEvento(evento) // Agrega evento en repositorio
                cargarEventos() // Recarga los eventos después de agregar
            } catch (e: Exception) {
                println("Error al agregar evento: ${e.message}")
            }
        }
    }

    fun eliminarEvento(idEvento: String) {
        viewModelScope.launch {
            try {
                empleadoRepository.eliminarEvento(idEvento) // Elimina evento del repositorio
                _eventos.value = _eventos.value.filter { it.id != idEvento } // Actualiza la lista de eventos localmente
            } catch (e: Exception) {
                println("Error al eliminar evento: ${e.message}")
            }
        }
    }

    fun actualizarEvento(evento: Evento) {
        viewModelScope.launch {
            try {
                empleadoRepository.actualizarEvento(evento) // Actualiza evento en el repositorio
                _eventos.value = _eventos.value.map {
                    if (it.id == evento.id) evento else it
                } // Actualiza la lista de eventos localmente
            } catch (e: Exception) {
                println("Error al actualizar evento: ${e.message}")
            }
        }
    }
}
