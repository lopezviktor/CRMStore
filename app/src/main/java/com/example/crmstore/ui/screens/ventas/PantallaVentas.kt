package com.example.crmstore.ui.screens.ventas

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
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
fun PantallaVentas(
    ventaViewModel: VentaViewModel,
    navHostController: NavHostController
) {
    val ventas = ventaViewModel.ventas

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Botón para ir a la pantalla de añadir ventas
        Button(
            onClick = { navHostController.navigate("PantallaAddVentas") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Añadir Nueva Venta")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Encabezado
        Text(text = "Ventas Existentes", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de ventas
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(ventas) { (id, venta) ->  // Desestructurar el Pair
                VentaItem(
                    venta = venta,  // Pasar solo el objeto Venta
                    onEliminarClick = {
                        ventaViewModel.eliminarVenta(id)  // Usar el ID para eliminar
                    }
                )
            }
        }
    }
}

@Composable
fun VentaItem(venta: Venta, onEliminarClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        // Información principal de la venta
        Text(text = "Cliente: ${venta.cliente}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Empleado: ${venta.empleado}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Total: ${venta.total} €", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(8.dp))

        // Detalles de los productos vendidos
        Text(text = "Productos:", style = MaterialTheme.typography.bodyMedium)
        venta.productosVendidos.forEach { producto ->
            Text(
                text = "- ${producto.nombre}: ${producto.precioUnitario} € (x${producto.cantidad})",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para eliminar la venta
        Button(
            onClick = onEliminarClick,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Eliminar Venta")
        }
    }
}