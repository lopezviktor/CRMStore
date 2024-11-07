package com.example.crmstore.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.crmstore.NavigationApp
import com.example.crmstore.modelo.MenuNavegador


@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { MenuNavegador(navController) }
    ) { innerPadding ->

        NavigationApp(navHostController = navController, modifier = Modifier.padding(innerPadding))
    }
}