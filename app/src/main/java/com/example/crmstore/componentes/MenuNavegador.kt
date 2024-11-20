package com.example.crmstore.componentes

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.unit.dp


@Composable
fun MenuNavegador(navHostController: NavHostController) {
    /*
    Esta función permite observar el estado de la entrada en la pila de navegación como un State.
    Esto significa que cada vez que cambia la pantalla (o ruta) actual, navBackStackEntry se actualiza automáticamente.
    */
    val navBackStackEntry = navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Surface(
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        shadowElevation = 4.dp
    ) {
    NavigationBar(
        containerColor = Color(0xD04959B6)
    ) {
        //CLIENTE
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Clientes", tint = Color.Black) },
            label = { Text("Clientes", color = Color.Black) },
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
            icon = { Icon(Icons.Filled.Inventory, contentDescription = "Productos", tint = Color.Black) },
            label = { Text("Productos", color = Color.Black) },
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
            icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Ventas", tint = Color.Black) },
            label = { Text("Ventas", color = Color.Black) },
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
            icon = { Icon(Icons.Filled.Group, contentDescription = "Empleados", tint = Color.Black) },
            label = { Text("Empleados", color = Color.Black) },
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
}