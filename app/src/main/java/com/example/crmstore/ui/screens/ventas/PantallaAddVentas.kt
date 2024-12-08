package com.example.crmstore.ui.screens.ventas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.crmstore.componentes.SeleccionDropdown
import com.example.crmstore.ui.viewmodel.VentaViewModel

@Composable
fun PantallaAddVentas(
    ventaViewModel: VentaViewModel,
    navHostController: NavHostController,
) {
    val clientes = ventaViewModel.clientes
    val empleados = ventaViewModel.empleados
    val clienteSeleccionado by ventaViewModel.clienteSeleccionado
    val empleadoSeleccionado by ventaViewModel.empleadoSeleccionado
    val carrito = ventaViewModel.carrito
    val productos = ventaViewModel.productos

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Encabezado
        Text(text = "Añadir Nueva Venta", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Seleccionar Cliente
        SeleccionDropdown(
            label = "Selecciona Cliente",
            opciones = clientes,
            seleccionado = clienteSeleccionado,
            onSeleccionar = { ventaViewModel.clienteSeleccionado.value = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Seleccionar Empleado
        SeleccionDropdown(
            label = "Selecciona Empleado",
            opciones = empleados,
            seleccionado = empleadoSeleccionado,
            onSeleccionar = { ventaViewModel.empleadoSeleccionado.value = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de productos disponibles
        Text(text = "Productos disponibles", style = MaterialTheme.typography.titleSmall)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(productos) { producto ->
                var cantidad by remember { mutableStateOf(1) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = producto.nombre)
                        Text(text = "${producto.precio} €")
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(onClick = { if (cantidad > 1) cantidad-- }) {
                            Text("-")
                        }
                        Text(text = "$cantidad", modifier = Modifier.padding(horizontal = 8.dp))
                        Button(onClick = { cantidad++ }) {
                            Text("+")
                        }
                    }

                    Button(onClick = {
                        ventaViewModel.agregarProductoAlCarrito(producto, cantidad)
                    }) {
                        Text("Añadir")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar productos en el carrito
        Text(text = "Productos en el Carrito", style = MaterialTheme.typography.titleSmall)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(carrito) { detalle ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${detalle.nombre} x${detalle.cantidad}")
                    Text(text = "${detalle.precioUnitario * detalle.cantidad} €")
                    Button(onClick = {
                        ventaViewModel.eliminarProductoDelCarrito(detalle.productoId)
                    }) {
                        Text("Eliminar")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar total del carrito
        val total = ventaViewModel.calcularTotalCarrito()
        Text(text = "Total: $total €", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para confirmar venta
        Button(
            onClick = {
                ventaViewModel.agregarVenta()
                navHostController.navigate("PantallaVentas") {
                    popUpTo("PantallaVentas") { inclusive = true } // Elimina esta pantalla del stack
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = carrito.isNotEmpty()
        ) {
            Text("Confirmar Venta")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para cancelar y volver a la pantalla anterior
        Button(
            onClick = {
                navHostController.popBackStack() // Vuelve a la pantalla anterior
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar")
        }
    }
}