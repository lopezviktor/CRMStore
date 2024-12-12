package com.example.crmstore.componentes

import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.crmstore.AuthManager
import com.example.crmstore.ui.theme.AzulOscuro
import com.example.crmstore.ui.theme.GrisOscuro2


@Composable
fun MenuNavegador(navController: NavHostController, authManager: AuthManager) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Surface(
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        shadowElevation = 6.dp,
        modifier = Modifier.height(98.dp).padding(top = 10.dp)
    ) {
        NavigationBar(
            containerColor = AzulOscuro,
        ) {
            // CLIENTES
            NavigationBarItem(
                icon = { Icon(
                    Icons.Filled.Person,
                    contentDescription = "Clientes",
                    tint = GrisOscuro2
                ) },
                label = {
                    Text(
                        "Clientes",
                        color = GrisOscuro2
                    )
                },
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
                icon = {
                    Icon(
                        Icons.Filled.Inventory,
                        contentDescription = "Productos",
                        tint = GrisOscuro2
                    )
                },
                label = {
                    Text(
                        "Productos",
                        color = GrisOscuro2
                    )
                },
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
                icon = {
                    Icon(
                        Icons.Filled.ShoppingCart,
                        contentDescription = "Ventas",
                        tint = GrisOscuro2
                    )
                },
                label = {
                    Text(
                        "Ventas",
                        color = GrisOscuro2
                    )
                },
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
                icon = {
                    Icon(
                        Icons.Filled.Group,
                        contentDescription = "Empleados",
                        tint = GrisOscuro2
                    )
                },
                label = {
                    Text(
                        "Empleados",
                        color = GrisOscuro2
                    )
                },
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
                icon = {
                    Icon(
                        Icons.Filled.ExitToApp,
                        contentDescription = "Cerrar Sesión",
                        tint = GrisOscuro2
                    )
                },
                label = {
                    Text(
                        "Cerrar Sesión",
                        color = GrisOscuro2
                    )
                },
                selected = false, // No necesita estar seleccionado
                onClick = {
                    authManager.logout(
                        onSuccess = {
                            navController.navigate("PantallaLogin") {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onFailure = { exception ->
                            Log.e("MenuNavegador", "Error al cerrar sesión", exception)
                        }
                    )
                }
            )
        }
    }
}