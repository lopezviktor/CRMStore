package com.example.crmstore

import PantallaAddCliente
import PantallaAddEmpleado
import PantallaAddProducto
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.crmstore.ui.screens.MainScreen
import com.example.crmstore.ui.screens.PantallaLogin
import com.example.crmstore.ui.screens.PantallaPerfil
import com.example.crmstore.ui.screens.PantallaRegistro
import com.example.crmstore.ui.screens.clientes.PantallaCliente
import com.example.crmstore.ui.screens.clientes.PantallaFormularioClientes
import com.example.crmstore.ui.screens.clientes.PantallaModificarCliente
import com.example.crmstore.ui.screens.empleados.PantallaEmpleado
import com.example.crmstore.ui.screens.empleados.PantallaFormularioEmpleados
import com.example.crmstore.ui.screens.eventos.PantallaAgenda
import com.example.crmstore.ui.screens.productos.PantallaFormularioProductos
import com.example.crmstore.ui.screens.ventas.PantallaAddVentas
import com.example.crmstore.ui.screens.ventas.PantallaDashboardVentas
import com.example.crmstore.ui.screens.ventas.PantallaVentas

@Composable
fun NavigationApp(navHostController: NavHostController, authManager: AuthManager, modifier: Modifier = Modifier) {

    val startDestination = if (authManager.isUserLoggedIn()) "PantallaFormularioEmpleados" else "PantallaLogin"

    NavHost(
        navController = navHostController,
        startDestination = startDestination,
    ) {
        // CLIENTES
        composable("PantallaCliente") { PantallaCliente(navHostController) }
        composable("PantallaAddCliente") { PantallaAddCliente(navHostController) }
        composable("PantallaFormularioClientes") {
            PantallaFormularioClientes(
                navHostController = navHostController,
                clienteViewModel = viewModel()
            )
        }
        composable(
            route = "PantallaModificarCliente/{idCliente}",
            arguments = listOf(navArgument("idCliente") { type = NavType.StringType })
        ) { backStackEntry ->
            val idCliente = backStackEntry.arguments?.getString("idCliente")
            PantallaModificarCliente(
                idCliente = idCliente,
                navHostController = navHostController
            )
        }


        //EMPLEADO
        composable("PantallaEmpleado") { PantallaEmpleado (navHostController) }
        composable("PantallaAddEmpleado") { PantallaAddEmpleado (navHostController) }
        composable("PantallaFormularioEmpleados") { PantallaFormularioEmpleados (navHostController) }
        composable("PantallaAgenda") { PantallaAgenda (navHostController)}

        //VENTAS
        composable("PantallaDashboardVentas") { PantallaDashboardVentas (navHostController) }
        composable("PantallaVentas") { PantallaVentas(ventaViewModel = viewModel(), navHostController = navHostController) }
        composable("PantallaAddVentas") { PantallaAddVentas(ventaViewModel = viewModel(), navHostController = navHostController) }

        //PRODUCTOS
        composable("PantallaAddProducto") { PantallaAddProducto(navHostController, productoViewModel = viewModel()) }
        composable("PantallaFormularioProductos") { PantallaFormularioProductos (navHostController, productoViewModel = viewModel()) }

        //LOGIN REGISTRO
        composable("PantallaLogin") { PantallaLogin (navHostController) }
        composable("PantallaRegistro") { PantallaRegistro (navHostController) }
        composable("PantallaPerfil") { PantallaPerfil (navHostController) }

        composable("MainScreen") { MainScreen(authManager, navHostController) }
    }
}