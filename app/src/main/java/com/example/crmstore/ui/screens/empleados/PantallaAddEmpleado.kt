package com.example.crmstore.ui.screens.empleados

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.crmstore.modelo.Empleado
import com.example.crmstore.modelo.Complemento
import com.example.crmstore.modelo.TipoComplemento
import com.example.crmstore.ui.viewmodel.EmpleadoViewModel

@Composable
fun PantallaAddEmpleado(
    navHostController: NavHostController,
    empleadoViewModel: EmpleadoViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var mail by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var puesto by remember { mutableStateOf("") }
    var salarioBase by remember { mutableStateOf("") }
    var complementos by remember { mutableStateOf(listOf<Complemento>()) }
    var showComplementoDialog by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

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
                "Añadir Nuevo Empleado",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = apellidos,
                onValueChange = { apellidos = it },
                label = { Text("Apellidos") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = mail,
                onValueChange = { mail = it },
                label = { Text("Mail") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = puesto,
                onValueChange = { puesto = it },
                label = { Text("Puesto") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = salarioBase,
                onValueChange = { salarioBase = it },
                label = { Text("Salario Base") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Lista de complementos
            complementos.forEach { complemento ->
                Text("${complemento.tipo}: ${complemento.valor}€", color = Color.White)
            }

            Button(onClick = { showComplementoDialog = true }) {
                Text("Añadir Complemento")
            }

            if (showComplementoDialog) {
                ComplementoDialog(
                    onDismiss = { showComplementoDialog = false },
                    onComplementoAdded = { nuevoComplemento ->
                        complementos += nuevoComplemento
                    }
                )
            }

            Button(
                onClick = {
                    val (esValido, mensaje) =
                        validarCampos(
                            nombre,
                            apellidos,
                            mail,
                            telefono,
                            puesto,
                            salarioBase
                        )
                    if (esValido) {
                        val nuevoEmpleado = Empleado(
                            nombre = nombre,
                            apellidos = apellidos,
                            mail = mail,
                            telefono = telefono,
                            puesto = puesto,
                            salarioBase = salarioBase.toDouble(),
                            complementos = complementos
                        )
                        empleadoViewModel.agregarEmpleado(nuevoEmpleado)
                        navHostController.popBackStack()
                    } else {
                        mensajeError = mensaje
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Guardar Empleado")
            }

            mensajeError?.let {
                Text(it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}

@Composable
fun ComplementoDialog(
    onDismiss: () -> Unit,
    onComplementoAdded: (Complemento) -> Unit
) {
    var tipoComplemento by remember { mutableStateOf(TipoComplemento.ANTIGUEDAD) }
    var valorComplemento by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Añadir Complemento") },
        text = {
            Column {

                DropdownMenu(
                    expanded = true,
                    onDismissRequest = {},
                    content = {
                        TipoComplemento.values().forEach { tipo ->
                            DropdownMenuItem(
                                text = { Text(tipo.name) },
                                onClick = {
                                    tipoComplemento = tipo

                                }
                            )
                        }
                    }
                )

                OutlinedTextField(
                    value = valorComplemento,
                    onValueChange = { valorComplemento = it },
                    label = { Text("Valor") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val valor = valorComplemento.toDoubleOrNull()
                if (valor != null) {
                    onComplementoAdded(Complemento(tipoComplemento, valor))
                    onDismiss()
                }
            }) {
                Text("Añadir")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

fun validarCampos(
    nombre: String,
    apellidos: String,
    mail: String,
    telefono: String,
    puesto: String,
    salarioBase: String
): Pair<Boolean, String?> {
    // Validar que los campos obligatorios no estén vacíos
    if (nombre.isBlank() || apellidos.isBlank() || mail.isBlank() || puesto.isBlank() || salarioBase.isBlank()) {
        return Pair(false, "Todos los campos son obligatorios excepto el teléfono.")
    }

    // Validar formato de mail
    val emailRegex =
        "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})".toRegex()

    if (!mail.matches(emailRegex)) {
        return Pair(false, "El formato del mail no es válido.")
    }

    // Validar que el teléfono solo contenga números (si se proporciona)
    if (telefono.isNotBlank() && !telefono.all { it.isDigit() }) {
        return Pair(false, "El teléfono solo debe contener números.")
    }

    // Validar que el salario base sea un número válido
    try {
        salarioBase.toDouble()
    } catch (e: NumberFormatException) {
        return Pair(false, "El salario base debe ser un número válido.")
    }

    // Si todas las validaciones pasan, retornar true
    return Pair(true, null)
}