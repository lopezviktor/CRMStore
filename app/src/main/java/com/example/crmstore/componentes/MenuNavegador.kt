package com.example.crmstore.componentes

import android.util.Log
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.crmstore.AuthManager


@Composable
fun MenuNavegador(navController: NavHostController, authManager: AuthManager) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Surface(
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        shadowElevation = 4.dp
    ) {
        NavigationBar(
            containerColor = Color(0xFF4976B6) // Fondo más claro para mayor contraste
        ) {
            // CLIENTES
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Person, contentDescription = "Clientes") },
                label = { Text("Clientes") },
                selected = currentRoute == "PantallaFormularioClientes",
                onClick = {
                    navController.navigate("PantallaFormularioClientes") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )

            // PRODUCTOS
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Inventory, contentDescription = "Productos") },
                label = { Text("Productos") },
                selected = currentRoute == "PantallaFormularioProductos",
                onClick = {
                    navController.navigate("PantallaFormularioProductos") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )

            // VENTAS
            NavigationBarItem(
                icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Ventas") },
                label = { Text("Ventas") },
                selected = currentRoute == "PantallaDashboardVentas",
                onClick = {
                    navController.navigate("PantallaDashboardVentas") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )

            // EMPLEADOS
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Group, contentDescription = "Empleados") },
                label = { Text("Empleados") },
                selected = currentRoute == "PantallaFormularioEmpleados",
                onClick = {
                    navController.navigate("PantallaFormularioEmpleados") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )

            // CIERRE DE SESIÓN
            NavigationBarItem(
                icon = { Icon(Icons.Filled.ExitToApp, contentDescription = "Cerrar Sesión") },
                label = { Text("Cerrar Sesión") },
                selected = false, // No necesita estar seleccionado
                onClick = {
                    authManager.logout( // Usa la función centralizada en AuthManager
                        onSuccess = {
                            navController.navigate("PantallaLogin") {
                                popUpTo(0) { inclusive = true } // Limpia el stack de navegación
                            }
                        },
                        onFailure = { exception ->
                            Log.e("MenuNavegador", "Error al cerrar sesión", exception)
                            // Puedes mostrar un mensaje de error al usuario si lo deseas
                        }
                    )
                }
            )
        }
    }
}