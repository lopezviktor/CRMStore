package com.example.crmstore

import PantallaAddCliente
import PantallaAddEmpleado
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.crmstore.ui.screens.PantallaInicio
import com.example.crmstore.ui.screens.PantallaLogin
import com.example.crmstore.ui.screens.PantallaPerfil
import com.example.crmstore.ui.screens.PantallaRegistro
//import com.example.crmstore.ui.screens.clientes.PantallaAddCliente
import com.example.crmstore.ui.screens.clientes.PantallaCliente
import com.example.crmstore.ui.screens.clientes.PantallaFormularioClientes
import com.example.crmstore.ui.screens.clientes.PantallaModificarCliente
import com.example.crmstore.ui.screens.empleados.PantallaEmpleado
import com.example.crmstore.ui.screens.empleados.PantallaFormularioEmpleados
import com.example.crmstore.ui.screens.productos.PantallaAddProducto
import com.example.crmstore.ui.screens.productos.PantallaFormularioProductos

import com.example.crmstore.ui.screens.productos.PantallaProducto
import com.example.crmstore.ui.screens.ventas.PantallaDashboardVentas
import com.example.crmstore.ui.screens.ventas.PantallaVentas
import com.example.crmstore.ui.viewmodel.ClienteViewModel
import com.example.crmstore.ui.viewmodel.VentaViewModel

@Composable
fun NavigationApp(navHostController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navHostController, startDestination = "PantallaVentas") {
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

        /*
        composable("PantallaCliente") { PantallaCliente(navHostController) }
        composable("PantallaAddCliente") { PantallaAddCliente(navHostController) }
        composable("PantallaFormularioClientes") { PantallaFormularioClientes(navHostController = navHostController, clienteViewModel = viewModel())
        composable(
            route = "PantallaModificarCliente/{idCliente}",
            arguments = listOf(navArgument("idCliente") { type = NavType.StringType })
        ) { backStackEntry ->
            val idCliente = backStackEntry.arguments?.getString("idCliente")
            PantallaModificarCliente(idCliente = idCliente, navHostController = navHostController)
        }
        }
        */

        //EMPLEADO
        composable("PantallaEmpleado") { PantallaEmpleado (navHostController) }
        composable("PantallaAddEmpleado") { PantallaAddEmpleado (navHostController) }
        composable("PantallaFormularioEmpleados") { PantallaFormularioEmpleados (navHostController) }

        //VENTAS
        composable("PantallaDashboardVentas") { PantallaDashboardVentas (navHostController) }
        composable("PantallaVentas") { PantallaVentas(ventaViewModel = VentaViewModel()) }

        //LOGIN
        composable("PantallaInicio") { PantallaInicio (navHostController) }
        composable("PantallaLogin") { PantallaLogin (navHostController) }
        composable("PantallaRegistro") { PantallaRegistro (navHostController) }
        composable("PantallaPerfil") { PantallaPerfil (navHostController) }

        //PRODUCTOS
        composable("PantallaProducto") { PantallaProducto (navHostController) }
        composable("PantallaAddProducto") { PantallaAddProducto (navHostController) }
        composable("PantallaFormularioProductos") { PantallaFormularioProductos (navHostController) }

        //LOGIN REGISTRO
        composable("PantallaLogin") { PantallaLogin (navHostController) }
        composable("PantallaRegistro") { PantallaRegistro (navHostController) }

    }
}