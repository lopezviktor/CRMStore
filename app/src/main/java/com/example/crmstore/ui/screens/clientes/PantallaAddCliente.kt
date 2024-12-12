
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.example.crmstore.componentes.BotonEstandar
import com.example.crmstore.modelo.Cliente
import com.example.crmstore.ui.theme.FondoPantallas
import com.example.crmstore.ui.theme.GrisOscuro2
import com.example.crmstore.ui.theme.Negro
import com.example.crmstore.ui.viewmodel.ClienteViewModel

@Composable
fun PantallaAddCliente(
    navHostController: NavHostController,
    clienteViewModel: ClienteViewModel = viewModel()
) {
    var dni by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var mail by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
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
                .padding(top = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Añadir Nuevo Cliente",
                style = MaterialTheme.typography.headlineMedium,
                color = GrisOscuro2,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = dni,
                onValueChange = { dni = it },
                label = { Text("DNI") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Negro),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Negro,
                    unfocusedTextColor = Negro,
                    disabledTextColor = Negro,
                    focusedLabelColor = Negro,
                    unfocusedLabelColor = Negro,
                    cursorColor = Negro
                )
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Negro),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Negro,
                    unfocusedTextColor = Negro,
                    disabledTextColor = Negro,
                    focusedLabelColor = Negro,
                    unfocusedLabelColor = Negro,
                    cursorColor = Negro
                )
            )

            OutlinedTextField(
                value = apellidos,
                onValueChange = { apellidos = it },
                label = { Text("Apellidos") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Negro),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Negro,
                    unfocusedTextColor = Negro,
                    disabledTextColor = Negro,
                    focusedLabelColor = Negro,
                    unfocusedLabelColor = Negro,
                    cursorColor = Negro
                )
            )

            OutlinedTextField(
                value = mail,
                onValueChange = { mail = it },
                label = { Text("Mail") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Negro,
                    unfocusedTextColor = Negro,
                    disabledTextColor = Negro,
                    focusedLabelColor = Negro,
                    unfocusedLabelColor = Negro,
                    cursorColor = Negro
                )
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Negro),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Negro,
                    unfocusedTextColor = Negro,
                    disabledTextColor = Negro,
                    focusedLabelColor = Negro,
                    unfocusedLabelColor = Negro,
                    cursorColor = Negro
                )
            )

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Negro),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Negro,
                    unfocusedTextColor = Negro,
                    disabledTextColor = Negro,
                    focusedLabelColor = Negro,
                    unfocusedLabelColor = Negro,
                    cursorColor = Negro
                )
            )

            BotonEstandar(
                texto = "Guardar Cliente",
                onClick = {
                    val (esValido, mensaje) = validarCamposCliente(dni, nombre, apellidos, mail, telefono)
                    if (esValido) {
                        val nuevoCliente = Cliente(
                            dni = dni,
                            nombre = nombre,
                            apellidos = apellidos,
                            mail = mail,
                            telefono = telefono.takeIf { it.isNotBlank() },
                            direccion = direccion.takeIf { it.isNotBlank() }
                        )
                        clienteViewModel.agregarCliente(nuevoCliente)
                        mostrarDialogoExito = true
                    } else {
                        mensajeErrorValidacion = mensaje ?: "Error de validación"
                        mostrarDialogoError = true
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
                onDismissRequest = { },
                title = { Text("Alta") },
                text = { Text("Cliente creado correctamente.") },
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
