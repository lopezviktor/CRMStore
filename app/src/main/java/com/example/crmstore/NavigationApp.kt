package com.example.crmstore

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.crmstore.ui.screens.PantallaInicio
import com.example.crmstore.ui.screens.PantallaLogin
import com.example.crmstore.ui.screens.PantallaPerfil
import com.example.crmstore.ui.screens.PantallaRegistro
import com.example.crmstore.ui.screens.clientes.PantallaAddCliente
import com.example.crmstore.ui.screens.clientes.PantallaCliente
import com.example.crmstore.ui.screens.clientes.PantallaFormularioClientes
import com.example.crmstore.ui.screens.empleados.PantallaEmpleado
import com.example.crmstore.ui.screens.empleados.PantallaAddEmpleado
import com.example.crmstore.ui.screens.empleados.PantallaFormularioEmpleados
import com.example.crmstore.ui.screens.productos.PantallaAddProducto
import com.example.crmstore.ui.screens.productos.PantallaFormularioProductos

import com.example.crmstore.ui.screens.productos.PantallaProducto
import com.example.crmstore.ui.screens.ventas.PantallaDashboardVentas

@Composable
fun NavigationApp(navHostController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navHostController, startDestination = "pantallaLogin") {
        // CLIENTES
        composable("PantallaCliente") { PantallaCliente(navHostController) }
        composable("PantallaAddCliente") { PantallaAddCliente(navHostController) }
        composable("PantallaFormularioClientes") { PantallaFormularioClientes (navHostController) }
        //EMPLEADO
        composable("PantallaEmpleado") { PantallaEmpleado (navHostController) }
        composable("PantallaAddEmpleado") { PantallaAddEmpleado (navHostController) }
        composable("PantallaFormularioEmpleados") { PantallaFormularioEmpleados (navHostController) }
        //VENTAS
        composable("PantallaDashboardVentas") { PantallaDashboardVentas (navHostController) }
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