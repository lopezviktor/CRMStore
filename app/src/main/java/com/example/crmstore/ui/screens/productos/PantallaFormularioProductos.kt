package com.example.crmstore.ui.screens.productos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.crmstore.modelo.Producto
import com.example.crmstore.ui.viewmodel.ProductoViewModel
import kotlinx.coroutines.delay

@Composable
fun PantallaFormularioProductos(
    navHostController: NavHostController,
    productoViewModel: ProductoViewModel = viewModel()
) {
    val productos by productoViewModel.productos.collectAsState()
    var mensajeBorrado by remember { mutableStateOf("") }
    var productoAEliminar by remember { mutableStateOf<Pair<String, Producto>?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    productoAEliminar?.let { (idDocumento, producto) ->
        AlertDialog(
            onDismissRequest = { productoAEliminar = null },
            title = { Text("Confirmación") },
            text = { Text("¿Estás seguro de que deseas eliminar '${producto.nombre}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        productoViewModel.eliminarProducto(idDocumento)
                        mensajeBorrado = "Producto eliminado correctamente"
                        productoAEliminar = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { productoAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1B88B6), Color(0xFF0A1D79))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
        ) {
            // Campo de búsqueda
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar producto") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Filtra los productos según el texto de búsqueda
            val filteredProductos = productos.filter {
                it.nombre.contains(searchQuery, ignoreCase = true)
            }

            // Lista de productos
            if (filteredProductos.isEmpty()) {
                Text(
                    text = "No se encontraron productos.",
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    items(filteredProductos) { producto ->
                        ProductoItem(
                            producto = producto,
                            onDelete = {
                                productoAEliminar = null // Ajusta el comportamiento de eliminación
                            }
                        )
                    }
                }
            }

            // Muestra mensaje de eliminación si existe
            if (mensajeBorrado.isNotEmpty()) {
                Text(
                    text = mensajeBorrado,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
                // Quita el mensaje después de 4 segundos
                LaunchedEffect(mensajeBorrado) {
                    delay(4000)
                    mensajeBorrado = ""
                }
            }

            // Botón para agregar nuevo producto
            FloatingActionButton(
                onClick = { navHostController.navigate("PantallaAddProducto") },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Producto")
            }
        }
    }
}

@Composable
fun ProductoItem(
    producto: Producto,
    onDelete: () -> Unit
) {
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Precio: ${producto.precio} €",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Stock: ${producto.stock}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar Producto")
            }
        }
    }
}