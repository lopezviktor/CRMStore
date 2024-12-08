package com.example.crmstore.ui.screens.clientes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

@Composable
fun PantallaModificarCliente(
    idCliente: String?, // ID del cliente a editar
    navHostController: NavHostController,
    clienteViewModel: ClienteViewModel = viewModel()
) {
    if (idCliente == null) {
        LaunchedEffect(Unit) {
            navHostController.popBackStack()
        }
        return
    }

    val cargando = clienteViewModel.cargando.collectAsState().value
    val clienteExistente = clienteViewModel.obtenerClientePorId(idCliente)

    // Si estamos cargando o no encontramos el cliente, mostramos el indicador de carga
    if (cargando || clienteExistente == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
        return
    }


    // Estados para almacenar los datos editables del cliente
    var dni by remember { mutableStateOf(clienteExistente.dni) }
    var nombre by remember { mutableStateOf(clienteExistente.nombre) }
    var apellidos by remember { mutableStateOf(clienteExistente.apellidos) }
    var mail by remember { mutableStateOf(clienteExistente.mail ?: "") }
    var telefono by remember { mutableStateOf(clienteExistente.telefono ?: "") }
    var direccion by remember { mutableStateOf(clienteExistente.direccion ?: "") }
    var mostrarDialogoExito by remember { mutableStateOf(false) }

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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Editar Cliente",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Campo DNI editable
            OutlinedTextField(
                value = dni,
                onValueChange = { dni = it },
                label = { Text("DNI") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )


            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = apellidos,
                onValueChange = { apellidos = it },
                label = { Text("Apellidos") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = mail,
                onValueChange = { mail = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Botón para guardar los cambios
            Button(
                onClick = {
                    val clienteActualizado = Cliente(
                        id = idCliente,
                        dni = dni,
                        nombre = nombre,
                        apellidos = apellidos,
                        mail = mail,
                        telefono = telefono,
                        direccion = direccion
                    )
                    clienteViewModel.actualizarCliente(idCliente, clienteActualizado)
                    mostrarDialogoExito = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Guardar Cambios")
            }
        }

        if (mostrarDialogoExito) {
            AlertDialog(
                onDismissRequest = { /* No hacemos nada para evitar el cierre automático */ },
                title = { Text("Actualizado") },
                text = { Text("Cliente actualizado correctamente.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            mostrarDialogoExito = false
                            navHostController.popBackStack() // Vuelve a la pantalla anterior
                        }
                    ) {
                        Text("Aceptar")
                    }
                }
            )
        }
    }
}