package com.example.crmstore

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavHostController
import com.example.crmstore.ui.screens.MainScreen

class MainActivity : ComponentActivity() {
    private lateinit var navHostController: NavHostController
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            authManager.handleGoogleSignInResult(
                result.data,
                onSuccess = {
                    Log.d("MainActivity", "Navegando a pantallaInicio después de autenticación exitosa.")
                    navHostController.navigate("pantallaInicio") {
                        popUpTo("pantallaLogin") { inclusive = true }
                    }
                },
                onFailure = { exception ->
                    Log.e("MainActivity", "Error en autenticación de Google", exception)
                }
            )
        }

        authManager = AuthManager(
            activity = this,
            clientId = getString(R.string.default_web_client_id),
            googleSignInLauncher = googleSignInLauncher
        )

        enableEdgeToEdge()
        setContent {
            MainScreen(authManager = authManager)
        }
    }

    fun signInWithGoogle() {
        authManager.signInWithGoogle()
    }
}