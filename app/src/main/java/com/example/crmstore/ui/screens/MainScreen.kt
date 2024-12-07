package com.example.crmstore.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.crmstore.AuthManager
import com.example.crmstore.NavigationApp
import com.example.crmstore.componentes.MenuNavegador


@Composable
fun MainScreen(authManager: AuthManager) {
    val navController = rememberNavController()
    val noMenuScreens = listOf("PantallaLogin", "PantallaRegistro")

    // Obtener la ruta actual
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute !in noMenuScreens) {
                MenuNavegador(navController)
            }
        }
    ) { innerPadding ->
        NavigationApp(
            navHostController = navController,
            authManager = authManager,
            modifier = Modifier.padding(innerPadding)
        )
    }
}