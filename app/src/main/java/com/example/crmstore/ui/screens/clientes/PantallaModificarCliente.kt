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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.crmstore.modelo.Cliente
import com.example.crmstore.ui.viewmodel.ClienteViewModel

@Composable
fun PantallaModificarCliente(
    idCliente: String?,
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

    if (cargando || clienteExistente == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
        return
    }

    var dni by remember { mutableStateOf(clienteExistente.dni) }
    var nombre by remember { mutableStateOf(clienteExistente.nombre) }
    var apellidos by remember { mutableStateOf(clienteExistente.apellidos) }
    var mail by remember { mutableStateOf(clienteExistente.mail ?: "") }
    var telefono by remember { mutableStateOf(clienteExistente.telefono ?: "") }
    var direccion by remember { mutableStateOf(clienteExistente.direccion ?: "") }
    var mostrarDialogoExito by remember { mutableStateOf(false) }
    var mostrarDialogoError by remember { mutableStateOf(false) }
    var mensajeErrorValidacion by remember { mutableStateOf("") }

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

            OutlinedTextField(
                value = dni,
                onValueChange = { dni = it },
                label = { Text("DNI") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.White,
                    focusedLabelColor = Color(0xFF90CAF9),
                    unfocusedLabelColor = Color(0xFF90CAF9),
                    cursorColor = Color.White
                )
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.White,
                    focusedLabelColor = Color(0xFF90CAF9),
                    unfocusedLabelColor = Color(0xFF90CAF9),
                    cursorColor = Color.White
                )
            )

            OutlinedTextField(
                value = apellidos,
                onValueChange = { apellidos = it },
                label = { Text("Apellidos") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.White,
                    focusedLabelColor = Color(0xFF90CAF9),
                    unfocusedLabelColor = Color(0xFF90CAF9),
                    cursorColor = Color.White
                )
            )

            OutlinedTextField(
                value = mail,
                onValueChange = { mail = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.White,
                    focusedLabelColor = Color(0xFF90CAF9),
                    unfocusedLabelColor = Color(0xFF90CAF9),
                    cursorColor = Color.White
                )
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.White,
                    focusedLabelColor = Color(0xFF90CAF9),
                    unfocusedLabelColor = Color(0xFF90CAF9),
                    cursorColor = Color.White
                )
            )

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.White,
                    focusedLabelColor = Color(0xFF90CAF9),
                    unfocusedLabelColor = Color(0xFF90CAF9),
                    cursorColor = Color.White
                )
            )

            Button(
                onClick = {
                    val (esValido, mensajeError) = validarCamposCliente(dni, nombre, apellidos, mail, telefono)
                    if (esValido) {
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
                    } else {
                        mostrarDialogoError = true
                        mensajeErrorValidacion = mensajeError ?: "Error de validación"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Guardar Cambios")
            }

            Spacer(modifier = Modifier.height(200.dp))
        }

        if (mostrarDialogoExito) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Actualizado") },
                text = { Text("Cliente actualizado correctamente.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            mostrarDialogoExito = false
                            navHostController.popBackStack()
                        }
                    ) {
                        Text("Aceptar")
                    }
                }
            )
        }

        if (mostrarDialogoError) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoError = false },
                title = { Text("Error de Validación") },
                text = { Text(mensajeErrorValidacion) },
                confirmButton = {
                    TextButton(onClick = { mostrarDialogoError = false }) {
                        Text("Aceptar")
                    }
                }
            )
        }
    }
}

fun validarCamposCliente(
    dni: String,
    nombre: String,
    apellidos: String,
    mail: String,
    telefono: String
): Pair<Boolean, String?> {
    // Validación de campos obligatorios y longitud máxima
    if (dni.isBlank() || nombre.isBlank() || apellidos.isBlank() || mail.isBlank()) {
        return Pair(false, "DNI, Nombre, Apellidos y Mail son obligatorios.")
    }
    if (dni.length > 9) {
        return Pair(false, "El DNI no puede tener más de 9 caracteres.")
    }
    if (nombre.length > 50 || apellidos.length > 50 || mail.length > 50) {
        return Pair(false, "Nombre, Apellidos y Mail no pueden exceder los 50 caracteres.")
    }

    // Validación de DNI
    val dniRegex = "^[0-9]{8}[TRWAGMYFPDXBNJZSQVHLCKE]$".toRegex()
    if (!dni.matches(dniRegex)) {
        return Pair(false, "El formato del DNI no es válido.")
    }

    // Validación de longitud y contenido del nombre y apellidos
    if (nombre.length < 2 || apellidos.length < 2) {
        return Pair(false, "Nombre y apellidos deben tener al menos 2 caracteres.")
    }
    if (nombre.any { it.isDigit() } || apellidos.any { it.isDigit() }) {
        return Pair(false, "Nombre y apellidos no deben contener números.")
    }

    // Validación de email
    val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,6}$".toRegex(RegexOption.IGNORE_CASE)
    if (!mail.matches(emailRegex)) {
        return Pair(false, "El formato del mail no es válido.")
    }

    // Validación de teléfono
    if (telefono.isNotBlank()) {
        if (!telefono.all { it.isDigit() }) {
            return Pair(false, "El teléfono solo debe contener números.")
        }
        if (telefono.length != 9) {
            return Pair(false, "El teléfono debe tener 9 dígitos.")
        }
    }

    return Pair(true, null)
}