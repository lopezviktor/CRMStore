package com.example.crmstore.ui.screens.ventas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.crmstore.modelo.Venta
import com.example.crmstore.ui.theme.AzulClaro
import com.example.crmstore.ui.theme.FondoPantallas
import com.example.crmstore.ui.theme.Negro
import com.example.crmstore.ui.theme.Rojizo
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
    var searchQuery by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = FondoPantallas
                )
            )
            .padding(bottom = 62.dp)

    ) {
        Column(modifier = Modifier.fillMaxSize().padding(top = 16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Campo de búsqueda
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Buscar venta") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    textStyle = TextStyle(color = Negro),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Negro,
                        unfocusedTextColor = Negro,
                        disabledTextColor = Negro,
                        focusedLabelColor = Negro,
                        unfocusedLabelColor = Negro,
                        cursorColor = Negro,
                        focusedBorderColor = AzulClaro,
                        unfocusedBorderColor = Negro
                    )
                )

                // Botón flotante para agregar una nueva venta
                FloatingActionButton(
                    onClick = { navHostController.navigate("PantallaAddVentas") },
                    containerColor = AzulClaro,
                    modifier = Modifier.size(56.dp) // Tamaño estándar de un FAB
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Añadir Venta",
                        tint = Negro
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Filtrar ventas según búsqueda
            val filteredVentas = ventas.filter { (_, venta) ->
                val cliente = ventaViewModel.clientes.find { it.id == venta.cliente }
                val empleado = ventaViewModel.empleados.find { it.id == venta.empleado }
                val clienteNombre =
                    cliente?.let { "${it.nombre} ${it.apellidos}" } ?: "Cliente desconocido"
                val empleadoNombre =
                    empleado?.let { "${it.nombre} ${it.apellidos}" } ?: "Empleado desconocido"

                clienteNombre.contains(searchQuery, ignoreCase = true) ||
                        empleadoNombre.contains(searchQuery, ignoreCase = true)
            }.sortedByDescending { it.second.fecha }

            if (filteredVentas.isEmpty()) {
                Text(
                    text = "No se encontraron ventas.",
                    color = Negro,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    items(filteredVentas) { (id, venta) ->
                        val cliente = ventaViewModel.clientes.find { it.id == venta.cliente }
                        val empleado = ventaViewModel.empleados.find { it.id == venta.empleado }
                        val clienteNombre =
                            cliente?.let { "${it.nombre} ${it.apellidos}" } ?: "Cliente desconocido"
                        val empleadoNombre = empleado?.let { "${it.nombre} ${it.apellidos}" }
                            ?: "Empleado desconocido"

                        VentaItem(
                            venta = venta,
                            clienteNombre = clienteNombre,
                            empleadoNombre = empleadoNombre,
                            onEliminarClick = { ventaViewModel.eliminarVenta(id) },
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun VentaItem(
    venta: Venta,
    clienteNombre: String,
    empleadoNombre: String,
    onEliminarClick: () -> Unit,
) {
    var showConfirmationDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Cliente: $clienteNombre",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Empleado: $empleadoNombre",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Total: ${venta.total} €",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Fecha: ${venta.fecha.toFormattedString()}",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Detalles:",
                    style = MaterialTheme.typography.titleSmall
                )

                venta.productosVendidos.forEach { detalle ->
                    Text(
                        text = "- ${detalle.cantidad}x ${detalle.nombre} a ${detalle.precioUnitario}€ por unidad",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            IconButton(
                onClick = { showConfirmationDialog = true },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar Venta",
                    tint = Rojizo
                )
            }

            if (showConfirmationDialog) {
                AlertDialog(
                    onDismissRequest = { showConfirmationDialog = false },
                    title = { Text("Confirmar eliminación") },
                    text = { Text("¿Estás seguro de que deseas eliminar esta venta? Esta acción no se puede deshacer.") },
                    confirmButton = {
                        TextButton(onClick = {
                            onEliminarClick()
                            showConfirmationDialog = false
                        }) {
                            Text("Eliminar", color = Rojizo)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showConfirmationDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}