package com.example.crmstore.ui.screens.productos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.crmstore.modelo.Producto
import com.example.crmstore.ui.theme.AzulClaro
import com.example.crmstore.ui.theme.FondoPantallas
import com.example.crmstore.ui.theme.Negro
import com.example.crmstore.ui.theme.Rojizo
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
    var productoSeleccionado by remember { mutableStateOf<Producto?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    // Diálogo de confirmación para eliminar producto
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
                    colors = FondoPantallas
                )
            )
            .padding(bottom = 80.dp)
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
                    label = { Text("Buscar producto") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    textStyle = TextStyle(color = Negro),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Negro,
                        unfocusedTextColor = Negro,
                    )
                )
                // Botón flotante para agregar productos
                FloatingActionButton(
                    onClick = { navHostController.navigate("PantallaAddProducto") },
                    containerColor = AzulClaro,
                    modifier = Modifier.size(56.dp) // Tamaño estándar de un FAB
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar Producto",
                        tint = Negro
                    )
                }
            }

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
                            onAdjustStock = { productoSeleccionado = it },
                            onDelete = {
                                productoAEliminar = producto.id to producto
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                // Quita el mensaje después de 4 segundos
                LaunchedEffect(mensajeBorrado) {
                    delay(4000)
                    mensajeBorrado = ""
                }
            }
        }
    }
    // Añadir el diálogo de ajuste de stock
    productoSeleccionado?.let { producto ->
        AjustarStockDialog(
            producto = producto,
            onDismiss = { productoSeleccionado = null },
            onConfirm = { nuevoStock ->
                productoViewModel.actualizarStock(producto.id, nuevoStock)
                productoSeleccionado = null
            }
        )
    }
}

@Composable
fun ProductoItem(
    producto: Producto,
    onAdjustStock: (Producto) -> Unit,
    onDelete: () -> Unit
) {
    val backgroundColor = if (producto.stock == 0) {
        Color(0x81E53F4E).copy(alpha = 0.5f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
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
            // Botón para ajustar stock
            IconButton(onClick = { onAdjustStock(producto) }) {
                Icon(Icons.Default.Edit, contentDescription = "Ajustar Stock")
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar Producto",
                    tint = Rojizo
                )
            }
        }
    }
}
@Composable
fun AjustarStockDialog(
    producto: Producto,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var nuevoStock by remember { mutableStateOf(producto.stock.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ajustar Stock") },
        text = {
            Column {
                Text("Producto: ${producto.nombre}")
                OutlinedTextField(
                    value = nuevoStock,
                    onValueChange = { nuevoStock = it },
                    label = { Text("Nuevo Stock") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val stockInt = nuevoStock.toIntOrNull()
                if (stockInt != null && stockInt >= 0) {
                    onConfirm(stockInt)
                }
            }) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}