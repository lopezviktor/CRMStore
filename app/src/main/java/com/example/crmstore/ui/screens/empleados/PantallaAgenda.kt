package com.example.crmstore.ui.screens.eventos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.crmstore.modelo.Evento
import com.example.crmstore.ui.viewmodel.EmpleadoViewModel

@Composable
fun PantallaAgenda(
    navController: NavHostController,
    empleadoViewModel: EmpleadoViewModel = viewModel()
) {
    val eventos by empleadoViewModel.eventos.collectAsState() // Observa los eventos desde el ViewModel

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Agenda de Eventos", style = MaterialTheme.typography.headlineMedium)

        LazyColumn {
            items(eventos) { evento ->
                EventoItem(evento) // Muestra cada evento en la lista
            }
        }
    }
}

@Composable
fun EventoItem(evento: Evento) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(evento.titulo, style = MaterialTheme.typography.titleLarge)
            Text("${evento.fecha} - ${evento.hora}", style = MaterialTheme.typography.bodyMedium)
            Text(evento.descripcion, style = MaterialTheme.typography.bodySmall)
        }
    }
}