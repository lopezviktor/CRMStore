package com.example.crmstore.ui.screens.clientes

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.crmstore.modelo.Cliente
import com.example.crmstore.ui.theme.FondoPantallas
import com.example.crmstore.ui.theme.Morado2
import com.example.crmstore.ui.theme.Negro
import com.example.crmstore.ui.viewmodel.ClienteViewModel
import kotlinx.coroutines.delay

@Composable
fun PantallaFormularioClientes(
    navHostController: NavHostController,
    clienteViewModel: ClienteViewModel = viewModel()
) {
    val clientes by clienteViewModel.clientes.collectAsState()
    var mensajeBorrado by remember { mutableStateOf("") }
    var clienteAEliminar by remember { mutableStateOf<Pair<String, Cliente>?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    // Diálogo de confirmación para eliminar cliente
    clienteAEliminar?.let { (idDocumento, cliente) ->
        AlertDialog(
            onDismissRequest = { clienteAEliminar = null },
            title = { Text("Confirmación") },
            text = { Text("¿Estás seguro de que deseas eliminar a '${cliente.nombre} ${cliente.apellidos}'?") },
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
                    label = { Text("Buscar cliente") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    textStyle = TextStyle(color = Negro),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Negro,
                        unfocusedTextColor = Negro,
                    )
                )
                // Botón para agregar nuevo cliente
                FloatingActionButton(
                    onClick = { navHostController.navigate("PantallaAddCliente") },
                    modifier = Modifier.size(56.dp) // Tamaño estándar de un FAB
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Añadir Cliente",
                        tint = Morado2
                    )
                }
            }
            // Filtra los clientes según el texto de búsqueda
            val filteredClientes = clientes.filter {
                it.second.nombre.contains(searchQuery, ignoreCase = true) ||
                        it.second.apellidos.contains(searchQuery, ignoreCase = true)
            }

            // Lista de clientes
            if (filteredClientes.isEmpty()) {
                Text(
                    text = "No se encontraron clientes.",
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
                    items(filteredClientes) { (idDocumento, cliente) ->
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
        }
    }
}

@Composable
fun ClienteItem(
    cliente: Cliente,
    onEdit: () -> Unit,
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
                    text = "${cliente.nombre} ${cliente.apellidos}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Email: ${cliente.mail ?: "No especificado"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Teléfono: ${cliente.telefono ?: "No especificado"}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar Cliente")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar Cliente")
            }
        }
    }
}
