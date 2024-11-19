package com.example.crmstore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crmstore.controlador.EmpleadoRepository
import com.example.crmstore.modelo.Empleado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EmpleadoViewModel : ViewModel() {

    private val empleadoRepository = EmpleadoRepository()

    private val _empleados = MutableStateFlow<List<Pair<String, Empleado>>>(emptyList())
    val empleados: StateFlow<List<Pair<String, Empleado>>> get() = _empleados

    init {
        cargarEmpleadosEnTiempoReal()
    }

    private fun cargarEmpleadosEnTiempoReal() {
        empleadoRepository.obtenerEmpleados { empleadosObtenidos ->
            _empleados.value = empleadosObtenidos
        }
    }

    fun eliminarEmpleado(idDocumento: String) {
        viewModelScope.launch {
            try {
                empleadoRepository.eliminarEmpleado(idDocumento)
                // Actualiza la lista local después de eliminar
                _empleados.value = _empleados.value.filter { it.first != idDocumento }
            } catch (e: Exception) {
                // Manejo de errores
                println("Error al eliminar empleado: ${e.message}")
                // Aquí podrías actualizar un estado de error si lo deseas
            }
        }
    }

    // Función para agregar un nuevo empleado
    fun agregarEmpleado(empleado: Empleado) {
        viewModelScope.launch {
            try {
                val nuevoId = empleadoRepository.agregarEmpleado(empleado)
                // Actualiza la lista local con el nuevo empleado
                _empleados.value = _empleados.value + (nuevoId to empleado)
            } catch (e: Exception) {
                println("Error al agregar empleado: ${e.message}")
            }
        }
    }

    // Función para actualizar un empleado existente
    fun actualizarEmpleado(idDocumento: String, empleadoActualizado: Empleado) {
        viewModelScope.launch {
            try {
                empleadoRepository.actualizarEmpleado(idDocumento, empleadoActualizado)
                // Actualiza la lista local con el empleado actualizado
                _empleados.value = _empleados.value.map {
                    if (it.first == idDocumento) idDocumento to empleadoActualizado else it
                }
            } catch (e: Exception) {
                println("Error al actualizar empleado: ${e.message}")
            }
        }
    }

    // Función para obtener un empleado específico por ID
    fun obtenerEmpleadoPorId(idDocumento: String): Empleado? {
        return _empleados.value.find { it.first == idDocumento }?.second
    }
}