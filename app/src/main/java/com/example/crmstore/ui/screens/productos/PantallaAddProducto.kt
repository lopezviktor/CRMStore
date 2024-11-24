package com.example.crmstore.ui.screens.productos

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
import com.example.crmstore.ui.viewmodel.ProductoViewModel

@Composable
fun PantallaAddProducto(
    navHostController: NavHostController,
    productoViewModel: ProductoViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
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
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Stock") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoría (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            mensajeError?.let {
                Text(it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}

fun validarCampos(
    nombre: String,
    descripcion: String,
    precio: String,
    stock: String,
    categoria: String
): Pair<Boolean, String?> {
    // Validar que los campos obligatorios no estén vacíos
    if (nombre.isBlank() || descripcion.isBlank() || precio.isBlank() || stock.isBlank()) {
        return Pair(false, "Todos los campos son obligatorios excepto la categoría.")
    }

    // Validar que el precio sea un número válido
    val precioDouble = precio.toDoubleOrNull()
    if (precioDouble == null || precioDouble <= 0) {
        return Pair(false, "El precio debe ser un número positivo válido.")
    }

    // Validar que el stock sea un número entero válido
    val stockInt = stock.toIntOrNull()
    if (stockInt == null || stockInt < 0) {
        return Pair(false, "El stock debe ser un número entero no negativo.")
    }

    // Si todas las validaciones pasan, retornar true
    return Pair(true, null)
}
