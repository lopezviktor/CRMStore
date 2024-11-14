package com.example.crmstore.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.crmstore.controlador.VentaRepository
import com.example.crmstore.modelo.Venta

class VentaViewModel : ViewModel() {
    private val ventaRepository = VentaRepository()

    val ventas = mutableStateListOf<Venta>()
    val mensaje = mutableStateOf("")

    init {
        ventas.addAll(ventaRepository.obtenerVentas())
    }

    fun agregarVenta(venta: Venta) {
        ventaRepository.agregarVenta(venta)
        ventas.add(venta)
        mensaje.value = "Venta añadida correctamente"
    }

    fun eliminarVenta(id: Int) {
        val fueEliminada = ventaRepository.eliminarVenta(id)
        if (fueEliminada) {
            val ventaAEliminar = ventas.find { it.id == id }
            if (ventaAEliminar != null) {
                ventas.remove(ventaAEliminar)
            }
            mensaje.value = "Venta eliminada exitosamente"
        } else {
            mensaje.value = "Error: Venta no encontrada"
        }
    }
}