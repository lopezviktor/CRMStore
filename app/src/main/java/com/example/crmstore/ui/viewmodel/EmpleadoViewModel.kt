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

    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> get() = _cargando

    init {
        // Cargar empleados y eventos en tiempo real
        cargarEmpleadosEnTiempoReal()
        cargarEventosEnTiempoReal()
    }


    private fun cargarEmpleadosEnTiempoReal() {
        empleadoRepository.obtenerEmpleados { empleadosObtenidos ->
            _empleados.value = empleadosObtenidos
            _cargando.value = false  // Cambiar estado de carga cuando se completan los datos
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

    fun obtenerEmpleadoPorId(idEmpleado: String): Empleado? {
        return _empleados.value.find { it.first == idEmpleado }?.second
    }

    fun actualizarEmpleado(idEmpleado: String, empleadoActualizado: Empleado) {
        viewModelScope.launch {
            try {
                val empleadoExistente = _empleados.value.find { it.first == idEmpleado }
                if (empleadoExistente == null) {
                    println("Error: No se encontró empleado con ID $idEmpleado.")
                    return@launch
                }

                empleadoRepository.actualizarEmpleado(idEmpleado, empleadoActualizado)
                _empleados.value = _empleados.value.map {
                    if (it.first == idEmpleado) idEmpleado to empleadoActualizado else it
                }
            } catch (e: Exception) {
                println("Error al actualizar empleado: ${e.message}")
            }
        }
    }


    // Métodos para agregar, eliminar o actualizar eventos

    fun agregarEvento(evento: Evento) {
        viewModelScope.launch {
            try {
                empleadoRepository.agregarEvento(evento) // Agrega evento en repositorio
            } catch (e: Exception) {
                println("Error al agregar evento: ${e.message}")
            }
        }
    }

    fun eliminarEvento(idEvento: String) {
        viewModelScope.launch {
            try {
                empleadoRepository.eliminarEvento(idEvento) // Elimina evento del repositorio
            } catch (e: Exception) {
                println("Error al eliminar evento: ${e.message}")
            }
        }
    }

    fun actualizarEvento(evento: Evento) {
        viewModelScope.launch {
            try {
                empleadoRepository.actualizarEvento(evento) // Actualiza evento en el repositorio
            } catch (e: Exception) {
                println("Error al actualizar evento: ${e.message}")
            }
        }
    }
}
