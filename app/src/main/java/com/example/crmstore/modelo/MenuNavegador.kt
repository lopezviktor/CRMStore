package com.example.crmstore.modelo

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.AccountBox

@Composable
fun MenuNavegador(navHostController: NavHostController) {
    val navBackStackEntry = navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(
        containerColor = Color.White
    ) {
        //CLIENTE
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Clientes") },
            label = { Text("Clientes") },
            selected = currentRoute == "PantallaCliente",
            onClick = {
                navHostController.navigate("PantallaFormularioClientes") {
                    popUpTo(navHostController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        //PRODUCTO
        NavigationBarItem(
            icon = { Icon(Icons.Filled.AccountBox, contentDescription = "Productos") },
            label = { Text("Productos") },
            selected = currentRoute == "PantallaFormularioProductos",
            onClick = {
                navHostController.navigate("PantallaFormularioProductos") {
                    popUpTo(navHostController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        //VENTAS
        NavigationBarItem(
            icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Ventas") },
            label = { Text("Ventas") },
            selected = currentRoute == "PantallaDashboardVentas",
            onClick = {
                navHostController.navigate("PantallaDashboardVentas") {
                    popUpTo(navHostController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        //EMPLEADOS
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Empleados") },
            label = { Text("Empleados") },
            selected = currentRoute == "PantallaFormularioEmpleados",
            onClick = {
                navHostController.navigate("PantallaFormularioEmpleados") {
                    popUpTo(navHostController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

    }
}