package com.example.crmstore.ui.screens.empleados

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.crmstore.modelo.Empleado
import com.example.crmstore.modelo.Evento
import com.example.crmstore.ui.viewmodel.EmpleadoViewModel
import kotlinx.coroutines.delay

@Composable
fun PantallaFormularioEmpleados(
    navHostController: NavHostController,
    empleadoViewModel: EmpleadoViewModel = viewModel()
) {
    val empleados by empleadoViewModel.empleados.collectAsState()
    val eventos by empleadoViewModel.eventos.collectAsState()

    var mensajeBorrado by remember { mutableStateOf("") }
    var empleadoAEliminar by remember { mutableStateOf<Pair<String, Empleado>?>(null) }
    var showAddEventDialog by remember { mutableStateOf(false) } // Estado para mostrar el diálogo de añadir evento

    var searchQuery by remember { mutableStateOf("") }

    // Diálogo de confirmación para eliminar empleado.
    empleadoAEliminar?.let { (idDocumento, empleado) ->
        AlertDialog(
            onDismissRequest = { empleadoAEliminar = null },
            title = { Text("Confirmación") },
            text = { Text("¿Estás seguro de que deseas eliminar a '${empleado.nombre} ${empleado.apellidos}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        empleadoViewModel.eliminarEmpleado(idDocumento)
                        mensajeBorrado = "Empleado eliminado correctamente"
                        empleadoAEliminar = null
                    }) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { empleadoAEliminar = null }) {
                    Text("Cancelar")
                }
            })
    }

    Box(
        modifier = Modifier.fillMaxSize().background(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF1B88B6), Color(0xFF0A1D79))
            )
        )
    ) {

        Column(modifier = Modifier.fillMaxSize().padding(top = 16.dp)) {

            // Campo de búsqueda.
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar empleado") },
                modifier = Modifier.fillMaxWidth().padding(horizontal=16.dp, vertical=8.dp)
            )

            // Resumen de próximos eventos.
            Text(
                "Próximos eventos",
                style=MaterialTheme.typography.titleMedium,
                modifier=Modifier.padding(16.dp)
            )

            // Botón "Añadir Evento"
            Button(
                onClick={ showAddEventDialog=true }, // Muestra el diálogo para añadir evento
                modifier=Modifier.fillMaxWidth().padding(horizontal=16.dp, vertical=8.dp)
            ) {
                Text("Añadir Evento")
            }

            if (eventos.isEmpty()) {
                Text("No hay próximos eventos.", Modifier.padding(16.dp))
            } else {
                LazyColumn(modifier=Modifier.height(100.dp).padding(horizontal=16.dp)) {
                    items(eventos.take(3)) { evento ->
                        EventoResumen(evento)
                    }
                }
            }

            // Filtra los empleados según el texto de búsqueda.
            val filteredEmpleados = empleados.filter {
                it.second.nombre.contains(searchQuery, ignoreCase=true) ||
                        it.second.apellidos.contains(searchQuery, ignoreCase=true)
            }

            if (filteredEmpleados.isEmpty()) {
                Text("No se encontraron empleados.", Modifier.padding(16.dp))
            } else {
                LazyColumn(modifier = Modifier.weight(1f).padding(16.dp)) {
                    items(filteredEmpleados) { (idDocumento, empleado) ->
                        EmpleadoItem(
                            empleado=empleado,
                            onEdit={ navHostController.navigate("PantallaModificarEmpleado/$idDocumento") },
                            onDelete={ empleadoAEliminar=idDocumento to empleado }
                        )
                    }
                }
            }

            // Muestra mensaje de eliminación si existe.
            if (mensajeBorrado.isNotEmpty()) {
                Text(text=mensajeBorrado, color=Color.Red, modifier=Modifier.padding(16.dp))

                // Quita el mensaje después de 4 segundos.
                LaunchedEffect(mensajeBorrado) {
                    delay(4000)
                    mensajeBorrado=""
                }
            }

            // Botón flotante para agregar nuevo empleado.
            FloatingActionButton(
                onClick={ navHostController.navigate("PantallaAddEmpleado") },
                modifier=Modifier.align(Alignment.End).padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription="Agregar Empleado")
            }
        }

    }

    // Diálogo para agregar un nuevo evento.
    if (showAddEventDialog) {
        AddEventoDialog(
            empleados=empleados, // Pasa la lista de empleados al diálogo
            onDismiss={ showAddEventDialog=false },
            onEventoAdded={ nuevoEvento ->
                empleadoViewModel.agregarEvento(nuevoEvento) // Agrega el nuevo evento al ViewModel
                showAddEventDialog=false
            })
    }
}

@Composable
fun EmpleadoItem(
    empleado: Empleado,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {

    Card(
        modifier=Modifier.fillMaxWidth().padding(8.dp),
        colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface),
        elevation=CardDefaults.cardElevation(defaultElevation=4.dp)
    ) {

        Row(modifier=Modifier.fillMaxWidth().padding(16.dp), verticalAlignment=Alignment.CenterVertically) {

            Column(modifier=Modifier.weight(1f)) {

                Text(text="${empleado.nombre} ${empleado.apellidos}", style=MaterialTheme.typography.bodyLarge)

                Text(text="Puesto:${empleado.puesto ?: "No especificado"}", style=MaterialTheme.typography.bodyMedium)

                Text(text="Salario Base:${empleado.salarioBase}€", style=MaterialTheme.typography.bodySmall)

            }

            IconButton(onClick=onEdit){ Icon(Icons.Default.Edit, contentDescription="Editar Empleado") }

            IconButton(onClick=onDelete){ Icon(Icons.Default.Delete, contentDescription="Eliminar Empleado") }

        }

    }

}

@Composable
fun EventoResumen(evento: Evento) {

    Row(modifier=Modifier.fillMaxWidth().padding(vertical=4.dp), horizontalArrangement=Arrangement.SpaceBetween) {

        Text(evento.fecha, style=MaterialTheme.typography.bodySmall)

        Text(evento.titulo, style=MaterialTheme.typography.bodyMedium)

    }

}

@Composable
fun AddEventoDialog(
    empleados: List<Pair<String, Empleado>>, // Lista de empleados para seleccionar
    onDismiss: () -> Unit,
    onEventoAdded: (Evento) -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    // Lista para mantener el estado de selección de empleados
    val selectedEmployees = remember { mutableStateListOf<String>() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Añadir Nuevo Evento") },
        text = {
            Column {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = fecha,
                    onValueChange = { fecha = it },
                    label = { Text("Fecha (DD/MM/YYYY)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = hora,
                    onValueChange = { hora = it },
                    label = { Text("Hora") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Lista de empleados con checkboxes para seleccionar participantes del evento.
                Text("Seleccionar Empleados", style = MaterialTheme.typography.bodyMedium)

                LazyColumn {
                    items(empleados) { (id, empleado) ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = selectedEmployees.contains(id),
                                onCheckedChange = {
                                    if (it) selectedEmployees.add(id)
                                    else selectedEmployees.remove(id)
                                }
                            )
                            Text("${empleado.nombre} ${empleado.apellidos}", modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val nuevoEvento = Evento(
                    titulo=titulo,
                    fecha=fecha,
                    hora=hora,
                    descripcion=descripcion,
                    participantes=selectedEmployees
                )

                onEventoAdded(nuevoEvento)

            }) {
                Text("Añadir")
            }
        },
        dismissButton={
            Button(onClick=onDismiss){
                Text("Cancelar")
            }
        }
    )
}