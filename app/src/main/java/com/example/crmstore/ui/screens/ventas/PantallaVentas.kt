package com.example.crmstore.ui.screens.ventas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.crmstore.modelo.Venta
import com.example.crmstore.ui.viewmodel.VentaViewModel

@Composable
fun PantallaVentas(ventaViewModel: VentaViewModel = viewModel()) {
    val ventas = ventaViewModel.ventas
    val mensaje by remember { ventaViewModel.mensaje }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Lista de Ventas", style = MaterialTheme.typography.headlineMedium)

        // Mostrar mensaje si existe
        if (mensaje.isNotEmpty()) {
            Text(text = mensaje, color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de ventas
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(ventas) { venta ->
                VentaItem(venta, onEliminarClick = { ventaViewModel.eliminarVenta(venta.id) })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para agregar una venta de ejemplo
        Button(
            onClick = {
                // Crear una venta de ejemplo y agregarla
                val nuevaVenta = Venta(
                    id = ventas.size + 1, // Generar un ID único
                    clienteId = 1,        // Asigna un cliente de ejemplo
                    empleadoId = 1,       // Asigna un empleado de ejemplo
                    fecha = "2024-11-15", // Fecha de ejemplo
                    productosVendidos = listOf(), // Productos vacíos para simplificar
                    total = 100.0         // Total de ejemplo
                )
                ventaViewModel.agregarVenta(nuevaVenta)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Agregar Venta de Prueba")
        }
    }
}

@Composable
fun VentaItem(venta: Venta, onEliminarClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Venta ID: ${venta.id}, Total: ${venta.total}", modifier = Modifier.weight(1f))
        Button(onClick = onEliminarClick) {
            Text("Eliminar")
        }
    }
}