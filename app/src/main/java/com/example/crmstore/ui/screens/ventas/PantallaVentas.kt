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
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

fun Timestamp.toFormattedString(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(this.toDate())
}

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
                VentaItem(
                    venta = venta,
                    onEliminarClick = { ventaViewModel.eliminarVenta(venta.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para agregar una venta de ejemplo
        Button(
            onClick = {
                val nuevaVenta = Venta(
                    id = ventas.size + 1,        // ID de la venta
                    clienteId = 1.toString(),    // Convertir el entero a String
                    empleadoId = 1,              // ID del empleado
                    fecha = Timestamp.now(),     // Fecha actual
                    productosVendidos = listOf(), // Lista vacía de productos (ejemplo)
                    total = 100.0                // Total de la venta
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
fun VentaItem(
    venta: Venta,
    onEliminarClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = "Venta ID: ${venta.id}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Fecha: ${venta.fecha.toFormattedString()}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Total: ${venta.total}", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onEliminarClick, modifier = Modifier.align(Alignment.End)) {
            Text("Eliminar")
        }
    }
}