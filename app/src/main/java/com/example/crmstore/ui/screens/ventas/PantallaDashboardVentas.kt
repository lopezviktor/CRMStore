package com.example.crmstore.ui.screens.ventas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.crmstore.ui.viewmodel.VentaViewModel

@Composable
fun PantallaDashboardVentas(
    viewModel: VentaViewModel,
    navHostController: NavHostController,
) {
    val totalVentas = viewModel.calcularTotalVentas()
    val totalVentasMes = viewModel.calcularTotalVentasDelMes()
    val promedioPorCliente = viewModel.calcularPromedioVentaPorCliente()
    val promedioPorClienteMes = viewModel.calcularPromedioVentaPorClienteDelMes()
    val topProductos = viewModel.obtenerTopProductosMasVendidos(5)
    val topProductosMes = viewModel.obtenerTopProductosMasVendidosDelMes(5)
    val clienteMasGastador = viewModel.obtenerClienteQueMasHaGastado()
    val empleadoMasVendedor = viewModel.obtenerEmpleadoQueMasHaVendido()

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
        ) {
            // Título
            Text(
                text = "DASHBOARD VENTAS",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para ir a la lista de ventas
            Button(
                onClick = { navHostController.navigate("PantallaVentas") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = "Ir a Lista de Ventas")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjetas deslizables
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Para que ocupe el espacio restante
            ) {
                item {
                    DashboardCardPager(
                        title = "Total Ventas",
                        firstValue = "$totalVentas €",
                        secondValue = "$totalVentasMes € (Mes)"
                    )
                }
                item {
                    DashboardCardPager(
                        title = "Promedio por Cliente",
                        firstValue = "$promedioPorCliente €",
                        secondValue = "$promedioPorClienteMes € (Mes)"
                    )
                }
                item {
                    DashboardCardPager(
                        title = "Top Productos",
                        firstValue = "Total:\n" + topProductos.joinToString("\n") { "${it.first}: ${it.second}" },
                        secondValue = "Mes Actual:\n" + topProductosMes.joinToString("\n") { "${it.first}: ${it.second}" }
                    )
                }
                item {
                    DashboardCard(
                        title = "Cliente & Empleado Destacados",
                        content = "Cliente: $clienteMasGastador\nEmpleado: $empleadoMasVendedor"
                    )
                }
            }
        }
    }
}
@Composable
fun DashboardCardPager(
    title: String,
    firstValue: String,
    secondValue: String
) {
    val pagerState = rememberPagerState(pageCount = { 2 })

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (page == 0) firstValue else secondValue,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardCard(
    title: String,
    content: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}