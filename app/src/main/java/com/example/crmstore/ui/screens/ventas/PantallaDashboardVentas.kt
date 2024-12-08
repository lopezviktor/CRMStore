package com.example.crmstore.ui.screens.ventas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    // Ejecutar la carga de ventas al iniciar
    LaunchedEffect(Unit) {
        ventaViewModel.cargarVentas()
    }

    // Acceder directamente a los datos del ViewModel
    val totalVentas = ventaViewModel.calcularTotalVentas()
    val productoMasVendido = ventaViewModel.obtenerProductoMasVendido()
    val promedioPorCliente = ventaViewModel.calcularPromedioVentaPorCliente()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "DASHBOARD VENTAS", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar datos calculados
        Text(text = "Total Ventas: ${totalVentas} €")
        Text(text = "Producto Más Vendido: $productoMasVendido")
        Text(text = "Promedio de Venta por Cliente: ${promedioPorCliente} €")

        Spacer(modifier = Modifier.height(16.dp))

        // Gráficos (Placeholder por ahora)
        Text(text = "Gráfico de Ventas por Mes (Aquí iría el gráfico)")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Productos Más Vendidos (Aquí iría el gráfico)")

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para ir a la lista de ventas
        Button(onClick = { navHostController.navigate("PantallaVentas") }) {
            Text(text = "Ver Detalle de Ventas")
        }
    }
}