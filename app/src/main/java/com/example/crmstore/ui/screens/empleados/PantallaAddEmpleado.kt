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
    //var mensajeErrorValidacion by remember { mutableStateOf<String?>(null) }
    var mostrarDialogoExito by remember { mutableStateOf(false) }
    var mensajeErrorValidacion by remember { mutableStateOf("") }
    var mostrarDialogoError by remember { mutableStateOf(false) }

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
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
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
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color(0xFF90CAF9),
                    unfocusedLabelColor = Color(0xFF90CAF9),
                    cursorColor = Color.White
                )
            )

            OutlinedTextField(
                value = mail,
                onValueChange = { mail = it },
                label = { Text("Mail") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color(0xFF90CAF9),
                    unfocusedLabelColor = Color(0xFF90CAF9),
                    cursorColor = Color.White
                )
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color(0xFF90CAF9),
                    unfocusedLabelColor = Color(0xFF90CAF9),
                    cursorColor = Color.White
                )
            )

            OutlinedTextField(
                value = puesto,
                onValueChange = { puesto = it },
                label = { Text("Puesto") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color(0xFF90CAF9),
                    unfocusedLabelColor = Color(0xFF90CAF9),
                    cursorColor = Color.White
                )
            )

            OutlinedTextField(
                value = salarioBase,
                onValueChange = { salarioBase = it },
                label = { Text("Salario Base") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color(0xFF90CAF9),
                    unfocusedLabelColor = Color(0xFF90CAF9),
                    cursorColor = Color.White
                )
            )

            Button(
                onClick = {
                    val (esValido, mensaje) = validarCamposEmpleado(nombre, apellidos, mail, telefono, puesto, salarioBase)
                    if (esValido) {
                        val nuevoEmpleado = Empleado(
                            nombre = nombre,
                            apellidos = apellidos,
                            mail = mail,
                            telefono = telefono.takeIf { it.isNotBlank() },
                            puesto = puesto.takeIf { it.isNotBlank() },
                            salarioBase = salarioBase.toDouble()
                        )
                        empleadoViewModel.agregarEmpleado(nuevoEmpleado)
                        mostrarDialogoExito = true
                    } else {
                        mostrarDialogoError = true
                        mensajeErrorValidacion = mensaje ?: "Error de validación"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Guardar Empleado")
            }

            Spacer(modifier = Modifier.height(16.dp))

            mensajeErrorValidacion?.let {
                Text(it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }
        }

        if (mostrarDialogoExito) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Alta") },
                text = { Text("Empleado creado correctamente.") },
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