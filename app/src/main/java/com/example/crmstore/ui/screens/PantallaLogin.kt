package com.example.crmstore.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.crmstore.MainActivity
import com.example.crmstore.R
import com.example.crmstore.componentes.BotonEstandar
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PantallaLogin(navHostController: NavHostController){
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()
    val activity = LocalContext.current as? MainActivity

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1B88B6), Color(0xFF0A1D79))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Iniciar Sesión",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff273270),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            BotonEstandar(
                texto = "Iniciar Sesión",
                onClick = {
                    if (usuario.isNotBlank() && password.isNotBlank()) {
                        auth.signInWithEmailAndPassword(usuario, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val currentUser = auth.currentUser
                                    currentUser?.let {
                                        mensajeError = "Bienvenido, ${it.email ?: "Usuario"}"
                                    }
                                    navHostController.navigate(route = "PantallaDashboardVentas") {
                                        popUpTo("PantallaLogin") { inclusive = true }
                                    }
                                } else {
                                    mensajeError = task.exception?.message ?: "Error de inicio de sesión"
                                }
                            }
                    } else {
                        mensajeError = "Por favor, ingrese email y contraseña."
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            // LaunchedEffect para manejar la navegación después del mensaje de bienvenida
            LaunchedEffect(mensajeError) {
                if (mensajeError.startsWith("Bienvenido")) {
                    kotlinx.coroutines.delay(10000)
                    navHostController.navigate(route = "PantallaDashboardVentas") {
                        popUpTo("PantallaLogin") { inclusive = true }
                    }
                }
            }
            if (mensajeError.isNotEmpty()) {
                Text(
                    text = mensajeError,
                    color = if (mensajeError.startsWith("Bienvenido")) Color.Green else Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { activity?.signInWithGoogle() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(
                        BorderStroke(2.dp, Color(0xff273270)),
                        shape = ButtonDefaults.shape
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4285F4)
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.iconogoogle),
                        contentDescription = "Google logo",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Iniciar Sesión con Google",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = {
                    navHostController.navigate("pantallaRegistro")
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Registrarse con correo electrónico", color = Color.White)
            }
        }
    }
}
