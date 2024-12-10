package com.example.crmstore.ui.screens.ventas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.crmstore.componentes.BotonEstandar
import com.example.crmstore.componentes.SeleccionDropdown
import com.example.crmstore.ui.theme.FondoPantallas
import com.example.crmstore.ui.theme.GrisOscuro2
import com.example.crmstore.ui.viewmodel.ProductoViewModel
import com.example.crmstore.ui.viewmodel.VentaViewModel

@Composable
fun PantallaAddVentas(
    ventaViewModel: VentaViewModel,
    navHostController: NavHostController,
    productoViewModel: ProductoViewModel
) {
    val clientes = ventaViewModel.clientes
    val empleados = ventaViewModel.empleados
    val clienteSeleccionado by ventaViewModel.clienteSeleccionado
    val empleadoSeleccionado by ventaViewModel.empleadoSeleccionado
    val carrito = ventaViewModel.carrito
    val productos = ventaViewModel.productos

    // Estado para mostrar el diálogo de alerta
    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = FondoPantallas
                )
            )
            .padding(8.dp)
            .padding(bottom = 62.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Encabezado
            Text(
                text = "Añadir Nueva Venta",
                style = MaterialTheme.typography.headlineSmall,
                color = GrisOscuro2,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
                    .padding(top = 6.dp)
            )

            SeleccionDropdown(
                label = "Selecciona Cliente",
                opciones = clientes,
                seleccionado = clienteSeleccionado,
                onSeleccionar = { ventaViewModel.clienteSeleccionado.value = it },
            )

            SeleccionDropdown(
                label = "Selecciona Empleado",
                opciones = empleados,
                seleccionado = empleadoSeleccionado,
                onSeleccionar = { ventaViewModel.empleadoSeleccionado.value = it },
            )

            // Lista de productos disponibles
            Text(
                text = "Productos disponibles",
                style = MaterialTheme.typography.titleSmall,
                color = GrisOscuro2,
                modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
            )

            LazyColumn(modifier = Modifier.weight(1f).padding(bottom = 8.dp)) {
                items(productos) { producto ->
                    var cantidad by remember { mutableStateOf(1) }
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = producto.nombre, style = MaterialTheme.typography.bodyLarge)
                                Text(text = "${producto.precio} €", style = MaterialTheme.typography.bodySmall)
                                Text(
                                    text = "Stock: ${producto.stock}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (producto.stock > 0) Color.Gray else Color.Red
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = { if (cantidad > 1) cantidad-- },
                                    enabled = producto.stock > 0
                                ) {
                                    Text("-")
                                }
                                Text(text = "$cantidad", modifier = Modifier.padding(horizontal = 8.dp))
                                Button(
                                    onClick = { cantidad++ },
                                    enabled = cantidad < producto.stock
                                ){
                                    Text("+")
                                }
                            }

                            Button(
                                onClick = {
                                    if (cantidad <= producto.stock) {
                                        ventaViewModel.agregarProductoAlCarrito(producto, cantidad)
                                    } else {
                                        alertMessage = "Stock insuficiente para el producto ${producto.nombre}"
                                        showAlert = true
                                    }
                                },
                                enabled = producto.stock > 0
                            ) {
                                Text("Añadir")
                            }
                        }
                    }
                }
            }
            // Carrito
            Text(
                text = "Productos en el carrito",
                style = MaterialTheme.typography.titleSmall,
                color = GrisOscuro2,
                modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
            )
            LazyColumn(modifier = Modifier.weight(1f).padding(bottom = 8.dp)) {
                items(carrito) { detalle ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Columna para los detalles del producto
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${detalle.nombre} x${detalle.cantidad}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "${detalle.precioUnitario} € por unidad",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            // Fila para los botones y el precio total
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = "${detalle.precioUnitario * detalle.cantidad} €",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Button(onClick = {
                                    ventaViewModel.eliminarProductoDelCarrito(detalle.productoId)
                                }) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }
            // Total del carrito y confirmar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val total = ventaViewModel.calcularTotalCarrito()
                Text(
                    text = "Total: $total €",
                    style = MaterialTheme.typography.titleMedium,
                    color = GrisOscuro2,
                    modifier = Modifier.padding(start = 8.dp)
                )
                BotonEstandar(
                    texto = "Confirmar Venta",
                    onClick = {
                        // Validar datos antes de proceder
                        when {
                            clienteSeleccionado == null -> {
                                alertMessage = "Por favor, selecciona un cliente."
                                showAlert = true
                            }
                            empleadoSeleccionado == null -> {
                                alertMessage = "Por favor, selecciona un empleado."
                                showAlert = true
                            }
                            carrito.isEmpty() -> {
                                alertMessage = "Por favor, añade al menos un producto al carrito."
                                showAlert = true
                            }
                            else -> {
                                ventaViewModel.agregarVenta()
                                productoViewModel.cargarProductos()
                                navHostController.navigate("PantallaVentas") {
                                    popUpTo("PantallaVentas") { inclusive = true }
                                }
                            }
                        }
                    },
                )
            }
        }
    }

    // Diálogo de alerta
    if (showAlert) {
        AlertDialog(
            onDismissRequest = { showAlert = false },
            confirmButton = {
                Button(onClick = { showAlert = false }) {
                    Text("Aceptar")
                }
            },
            title = { Text("Error") },
            text = { Text(alertMessage) }
        )
    }
}