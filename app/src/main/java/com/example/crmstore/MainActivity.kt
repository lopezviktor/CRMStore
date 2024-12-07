package com.example.crmstore

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.crmstore.ui.screens.MainScreen

class MainActivity : ComponentActivity() {
    private lateinit var navHostController: NavHostController
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuración del inicio de sesión con Google
        val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            authManager.handleGoogleSignInResult(
                result.data,
                onSuccess = {
                    Log.d("MainActivity", "Inicio de sesión exitoso con Google.")
                    if (::navHostController.isInitialized) {
                        navHostController.navigate("PantallaDashboardVentas") {
                            popUpTo("PantallaLogin") { inclusive = true }
                        }
                    } else {
                        Log.e("MainActivity", "navHostController no inicializado")
                    }
                },
                onFailure = { exception ->
                    Log.e("MainActivity", "Error en autenticación de Google", exception)
                }
            )
        }

        // Inicializar AuthManager
        authManager = AuthManager(
            activity = this,
            clientId = getString(R.string.default_web_client_id),
            googleSignInLauncher = googleSignInLauncher
        )

        // Configurar UI
        enableEdgeToEdge()
        setContent {
            navHostController = rememberNavController()
            MainScreen(authManager = authManager, navController = navHostController)
        }
    }

    fun signInWithGoogle() {
        authManager.signInWithGoogle()
    }
}