package com.example.crmstore.ui.screens.clientes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.crmstore.modelo.Cliente
import com.example.crmstore.ui.viewmodel.ClienteViewModel
import kotlinx.coroutines.delay

@Composable
fun PantallaFormularioClientes(
    navHostController: NavHostController,
    clienteViewModel: ClienteViewModel = viewModel()
) {
    // Observa el flujo de clientes desde el ViewModel
    val clientes by clienteViewModel.clientes.collectAsState(emptyList())
    var mensajeBorrado by remember { mutableStateOf("") }
    var clienteAEliminar by remember { mutableStateOf<Pair<String, Cliente>?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    // Dialogo de confirmación para eliminar cliente
    clienteAEliminar?.let { (idDocumento, cliente) ->
        AlertDialog(
            onDismissRequest = { clienteAEliminar = null },
            title = { Text("Confirmación") },
            text = { Text("¿Estás seguro de que deseas eliminar '${cliente.nombre}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        clienteViewModel.eliminarCliente(idDocumento)
                        mensajeBorrado = "Cliente eliminado correctamente"
                        clienteAEliminar = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { clienteAEliminar = null }) {
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
                label = { Text("Buscar cliente") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Filtra los clientes según el texto de búsqueda
            val filteredClients = clientes.filter {
                it.second.nombre.contains(searchQuery, ignoreCase = true)
            }

            if (filteredClients.isEmpty()) {
                Text("No se encontraron clientes.", Modifier.padding(16.dp))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    items(filteredClients) { (idDocumento, cliente) ->
                        ClienteItem(
                            cliente = cliente,
                            onEdit = {
                                navHostController.navigate("PantallaModificarCliente/$idDocumento")
                            },
                            onDelete = {
                                clienteAEliminar = idDocumento to cliente
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

            // Botón para agregar nuevo cliente
            Button(
                onClick = { navHostController.navigate("PantallaAddCliente") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Agregar Cliente")
            }
        }
    }
}

@Composable
fun ClienteItem(cliente: Cliente, onEdit: (Cliente) -> Unit, onDelete: (Cliente) -> Unit) {
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
                // Nombre completo del cliente
                Text(
                    text = "${cliente.nombre} ${cliente.apellidos}",
                    style = MaterialTheme.typography.bodyLarge
                )
                // Email del cliente
                Text(
                    text = "Email: ${cliente.mail}",
                    style = MaterialTheme.typography.bodySmall
                )
                // Teléfono del cliente (si está disponible)
                cliente.telefono?.let {
                    Text(
                        text = "Teléfono: $it",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            IconButton(onClick = { onEdit(cliente) }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar Cliente")
            }
            IconButton(onClick = { onDelete(cliente) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar Cliente")
            }
        }
    }
}