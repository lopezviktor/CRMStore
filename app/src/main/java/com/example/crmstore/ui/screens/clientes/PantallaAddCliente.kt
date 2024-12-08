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
import com.example.crmstore.modelo.Cliente
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
                "Añadir Nuevo Cliente",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = dni,
                onValueChange = { dni = it },
                label = { Text("DNI") },
                modifier = Modifier.fillMaxWidth()
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
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
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
                        navHostController.popBackStack()
                    } else {
                        mensajeError = mensaje
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Guardar Cliente")
            }

            mensajeError?.let {
                Text(it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }
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
    if (dni.isBlank() || nombre.isBlank() || apellidos.isBlank() || mail.isBlank()) {
        return Pair(false, "DNI, Nombre, Apellidos y Mail son obligatorios.")
    }

    val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})".toRegex()
    if (!mail.matches(emailRegex)) {
        return Pair(false, "El formato del mail no es válido.")
    }

    if (telefono.isNotBlank() && !telefono.all { it.isDigit() }) {
        return Pair(false, "El teléfono solo debe contener números.")
    }

    return Pair(true, null)
}
