
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.crmstore.modelo.Producto
import com.example.crmstore.ui.viewmodel.ProductoViewModel

@Composable
fun PantallaAddProducto(
    navHostController: NavHostController,
    productoViewModel: ProductoViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
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
                "Añadir Nuevo Producto",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .padding(top = 20.dp)
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Producto") },
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
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio") },
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

            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Stock") },
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

            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoría") },
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
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
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
                    val (esValido, mensaje) = validarCamposProducto(nombre, precio, stock, categoria)
                    if (esValido) {
                        val nuevoProducto = Producto(
                            id = "", // Se genera automáticamente en Firestore
                            nombre = nombre,
                            precio = precio.toDouble(),
                            stock = stock.toInt(),
                            categoria = categoria,
                            descripcion = descripcion
                        )
                        productoViewModel.agregarProducto(nuevoProducto)
                        navHostController.popBackStack()
                    } else {
                        mensajeError = mensaje
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Guardar Producto")
            }

            mensajeError?.let {
                Text(it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}

fun validarCamposProducto(
    nombre: String,
    precio: String,
    stock: String,
    categoria: String
): Pair<Boolean, String?> {
    // Validar que los campos obligatorios no estén vacíos
    if (nombre.isBlank() || precio.isBlank() || stock.isBlank() || categoria.isBlank()) {
        return Pair(false, "Todos los campos son obligatorios.")
    }

    // Validar que el precio sea un número válido
    try {
        precio.toDouble()
    } catch (e: NumberFormatException) {
        return Pair(false, "El precio debe ser un número válido.")
    }

    // Validar que el stock sea un número válido
    try {
        stock.toInt()
    } catch (e: NumberFormatException) {
        return Pair(false, "El stock debe ser un número válido.")
    }

    // Si todas las validaciones pasan, retornar true
    return Pair(true, null)
}