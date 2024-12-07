package com.example.crmstore

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthManager(
    private val activity: Activity,
    clientId: String,
    private val googleSignInLauncher: ActivityResultLauncher<Intent>
) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val googleSignInClient: GoogleSignInClient

    init {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientId)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(activity, googleSignInOptions)
    }

    fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    fun handleGoogleSignInResult(data: Intent?, onSuccess: (String) -> Unit, onFailure: (Exception?) -> Unit) {
        Log.d("AuthManager", "Manejando el resultado de Google Sign-In.")
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            Log.d("AuthManager", "ID Token obtenido: ${account.idToken}")
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(activity) { authTask ->
                    if (authTask.isSuccessful) {
                        Log.d("AuthManager", "Autenticación en Firebase exitosa.")
                        onSuccess("Autenticación con Google exitosa.")
                    } else {
                        Log.e("AuthManager", "Error en la autenticación con Firebase.", authTask.exception)
                        onFailure(authTask.exception)
                    }
                }
        } catch (e: ApiException) {
            Log.e("AuthManager", "Error al obtener la cuenta de Google: ${e.localizedMessage}")
            onFailure(e)
        }
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun signOut() {
        auth.signOut()
        googleSignInClient.signOut()
    }
}