package com.example.crmstore.ui.screens.ventas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.crmstore.ui.viewmodel.VentaViewModel

@Composable
fun PantallaDashboardVentas(
    navHostController: NavHostController,
    ventaViewModel: VentaViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "DASHBOARD VENTAS", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Total Ventas: 5000 €")
        Text(text = "Productos Más Vendidos: Camisetas")
        Text(text = "Promedio de Venta por Cliente: 150 €")

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Gráfico de Ventas por Mes (Aquí iría el gráfico)")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Productos Más Vendidos (Aquí iría el gráfico)")

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para ver el detalle de las ventas en PantallaVentas
        Button(onClick = { navHostController.navigate("pantallaVentas") }) {
            Text(text = "Ver Detalle de Ventas")
        }
    }
}