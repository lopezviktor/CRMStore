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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.crmstore.modelo.Producto
import com.example.crmstore.ui.viewmodel.VentaViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

fun Timestamp.toFormattedString(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(this.toDate())
}

@Composable
fun PantallaVentas(ventaViewModel: VentaViewModel) {
    val carrito = ventaViewModel.carrito
    val productos = listOf(
        Producto(nombre = "Camisa", precio = 20.0),
        Producto(nombre = "Pantalón", precio = 30.0),
        Producto(nombre = "Zapatos", precio = 50.0)
    )
    val ventas = ventaViewModel.ventas // Lista de ventas con IDs

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Carrito de Compras", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de productos disponibles
        Text(text = "Productos disponibles", style = MaterialTheme.typography.titleSmall)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(productos) { producto ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = producto.nombre)
                    Text(text = "${producto.precio} €")
                    Button(onClick = {
                        ventaViewModel.agregarProductoAlCarrito(producto, 1)
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
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = carrito.isNotEmpty()
        ) {
            Text("Confirmar Venta")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar ventas existentes
        Text(text = "Ventas existentes", style = MaterialTheme.typography.titleSmall)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(ventas) { (id, venta) ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(text = "ID: $id")
                    Text(text = "Cliente: ${venta.cliente}")
                    Text(text = "Empleado: ${venta.empleado}")
                    Text(text = "Total: ${venta.total} €")
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}