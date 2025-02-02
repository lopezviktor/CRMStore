package com.example.crmstore.ui.screens.empleados

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.crmstore.componentes.BotonEstandar
import com.example.crmstore.modelo.Empleado
import com.example.crmstore.ui.theme.AzulClaro
import com.example.crmstore.ui.theme.FondoPantallas
import com.example.crmstore.ui.theme.GrisOscuro2
import com.example.crmstore.ui.theme.Negro
import com.example.crmstore.ui.viewmodel.EmpleadoViewModel

@Composable
fun PantallaModificarEmpleado(
    idEmpleado: String?, // ID del empleado a editar
    navHostController: NavHostController,
    empleadoViewModel: EmpleadoViewModel = viewModel()
) {
    if (idEmpleado == null) {
        LaunchedEffect(Unit) {
            navHostController.popBackStack()
        }
        return
    }

    val cargando by empleadoViewModel.cargando.collectAsState()// Accede al valor de cargando directamente
    val empleadoExistente = empleadoViewModel.obtenerEmpleadoPorId(idEmpleado)

    // Si estamos cargando o no encontramos el empleado, mostramos el indicador de carga
    if (cargando || empleadoExistente == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
        return
    }

    // Estados para almacenar los datos editables del empleado
    var nombre by remember { mutableStateOf(empleadoExistente.nombre) }
    var apellidos by remember { mutableStateOf(empleadoExistente.apellidos) }
    var mail by remember { mutableStateOf(empleadoExistente.mail ?: "") }
    var telefono by remember { mutableStateOf(empleadoExistente.telefono ?: "") }
    var puesto by remember { mutableStateOf(empleadoExistente.puesto ?: "") }
    //var salarioBase by remember { mutableStateOf(empleadoExistente.salarioBase) }
    var salarioBase by remember { mutableStateOf(empleadoExistente.salarioBase.toString()) }
    var pagas by remember { mutableStateOf(empleadoExistente.pagas) }
    var mostrarDialogoExito by remember { mutableStateOf(false) }
    var mostrarDialogoError by remember { mutableStateOf(false) }
    var mensajeErrorValidacion by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = FondoPantallas
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
                "Editar Empleado",
                style = MaterialTheme.typography.headlineMedium,
                color = GrisOscuro2,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .padding(top = 20.dp)
            )

            // Campos editables para los atributos del empleado
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier
                    .fillMaxWidth(),
                    //.padding(vertical = 8.dp),
                textStyle = TextStyle(color = Negro),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Negro,
                    unfocusedTextColor = Negro,
                    disabledTextColor = Negro,
                    focusedLabelColor = Negro,
                    unfocusedLabelColor = Negro,
                    cursorColor = Negro,
                    focusedBorderColor = AzulClaro,
                    unfocusedBorderColor = Negro
                )
            )

            OutlinedTextField(
                value = apellidos,
                onValueChange = { apellidos = it },
                label = { Text("Apellidos") },
                modifier = Modifier
                    .fillMaxWidth(),
                    //.padding(vertical = 8.dp),
                textStyle = TextStyle(color = Negro),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Negro,
                    unfocusedTextColor = Negro,
                    disabledTextColor = Negro,
                    focusedLabelColor = Negro,
                    unfocusedLabelColor = Negro,
                    cursorColor = Negro,
                    focusedBorderColor = AzulClaro,
                    unfocusedBorderColor = Negro
                )
            )

            OutlinedTextField(
                value = mail,
                onValueChange = { mail = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth(),
                    //.padding(vertical = 8.dp),
                textStyle = TextStyle(color = Negro),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Negro,
                    unfocusedTextColor = Negro,
                    disabledTextColor = Negro,
                    focusedLabelColor = Negro,
                    unfocusedLabelColor = Negro,
                    cursorColor = Negro,
                    focusedBorderColor = AzulClaro,
                    unfocusedBorderColor = Negro
                )
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                modifier = Modifier
                    .fillMaxWidth(),
                    //.padding(vertical = 8.dp),
                textStyle = TextStyle(color = Negro),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Negro,
                    unfocusedTextColor = Negro,
                    disabledTextColor = Negro,
                    focusedLabelColor = Negro,
                    unfocusedLabelColor = Negro,
                    cursorColor = Negro,
                    focusedBorderColor = AzulClaro,
                    unfocusedBorderColor = Negro
                )
            )

            OutlinedTextField(
                value = puesto,
                onValueChange = { puesto = it },
                label = { Text("Puesto") },
                modifier = Modifier
                    .fillMaxWidth(),
                    //.padding(vertical = 8.dp),
                textStyle = TextStyle(color = Negro),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Negro,
                    unfocusedTextColor = Negro,
                    disabledTextColor = Negro,
                    focusedLabelColor = Negro,
                    unfocusedLabelColor = Negro,
                    cursorColor = Negro,
                    focusedBorderColor = AzulClaro,
                    unfocusedBorderColor = Negro
                )
            )

            OutlinedTextField(
                value = salarioBase.toString(),
                //onValueChange = { salarioBase = it.toDoubleOrNull() ?: 0.0 },
                onValueChange = {
                    salarioBase = it
                },
                label = { Text("Salario Base (€)") },
                modifier = Modifier
                    .fillMaxWidth(),
                    //.padding(vertical = 8.dp),
                textStyle = TextStyle(color = Negro),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Negro,
                    unfocusedTextColor = Negro,
                    disabledTextColor = Negro,
                    focusedLabelColor = Negro,
                    unfocusedLabelColor = Negro,
                    cursorColor = Negro,
                    focusedBorderColor = AzulClaro,
                    unfocusedBorderColor = Negro
                )
            )

            OutlinedTextField(
                value = pagas.toString(),
                onValueChange = { pagas = it.toIntOrNull() ?: 12 },
                label = { Text("Pagas") },
                modifier = Modifier
                    .fillMaxWidth(),
                    //.padding(vertical = 8.dp),
                textStyle = TextStyle(color = Negro),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Negro,
                    unfocusedTextColor = Negro,
                    disabledTextColor = Negro,
                    focusedLabelColor = Negro,
                    unfocusedLabelColor = Negro,
                    cursorColor = Negro,
                    focusedBorderColor = AzulClaro,
                    unfocusedBorderColor = Negro
                )
            )

            // Botón para guardar los cambios
            BotonEstandar(
                texto = "Guardar Cambios",
                onClick = {
                    val (esValido, mensaje) = validarCamposEmpleado(nombre, apellidos, mail, telefono, puesto, salarioBase)
                    if (esValido) {
                        val salarioDouble = salarioBase.toDoubleOrNull() ?: 0.0
                        val empleadoActualizado = Empleado(
                            id = idEmpleado,
                            nombre = nombre,
                            apellidos = apellidos,
                            mail = mail,
                            telefono = telefono,
                            puesto = puesto,
                            salarioBase = salarioDouble,
                            pagas = pagas
                        )
                        empleadoViewModel.actualizarEmpleado(idEmpleado, empleadoActualizado)
                        mostrarDialogoExito = true
                    } else {
                        mostrarDialogoError = true
                        mensajeErrorValidacion = mensaje ?: "Error de validación"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

            Spacer(modifier = Modifier.height(200.dp))
        }

        if (mostrarDialogoExito) {
            AlertDialog(
                onDismissRequest = { /* No hacemos nada para evitar el cierre automático */ },
                title = { Text("Actualizado") },
                text = { Text("Empleado actualizado correctamente.") },
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

fun validarCamposEmpleado(
    nombre: String,
    apellidos: String,
    mail: String,
    telefono: String,
    puesto: String,
    salarioBase: String
): Pair<Boolean, String?> {
    if (nombre.isBlank() || apellidos.isBlank() || mail.isBlank() || salarioBase.isBlank()) {
        return Pair(false, "Nombre, Apellidos, Mail y Salario Base son obligatorios.")
    }

    if (nombre.length > 50 || apellidos.length > 50 || mail.length > 50) {
        return Pair(false, "Nombre, Apellidos y Mail no pueden exceder los 50 caracteres.")
    }

    // Validación de longitud y contenido del nombre y apellidos
    if (nombre.length < 2 || apellidos.length < 2) {
        return Pair(false, "Nombre y apellidos deben tener al menos 2 caracteres.")
    }
    if (nombre.any { it.isDigit() } || apellidos.any { it.isDigit() }) {
        return Pair(false, "Nombre y apellidos no deben contener números.")
    }

    val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,6}$".toRegex(RegexOption.IGNORE_CASE)
    if (!mail.matches(emailRegex)) {
        return Pair(false, "El formato del mail no es válido.")
    }

    if (telefono.isNotBlank() && (!telefono.all { it.isDigit() } || telefono.length != 9)) {
        return Pair(false, "El teléfono debe ser un número de 9 dígitos.")
    }

    val salarioDouble = salarioBase.toDoubleOrNull()
    if (salarioDouble == null || salarioDouble <= 0) {
        return Pair(false, "El salario base debe ser un número positivo.")
    }

    if (puesto.isNotBlank() && puesto.length < 2) {
        return Pair(false, "El puesto debe tener al menos 2 caracteres.")
    }

    return Pair(true, null)
}