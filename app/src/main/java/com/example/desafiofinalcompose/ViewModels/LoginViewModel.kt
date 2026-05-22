package com.example.loginfirebase_25_26

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.desafiofinalcompose.DatosCompartidos
import com.example.desafiofinalcompose.Models.Usuario
import com.example.desafiofinalcompose.R
import com.example.desafiofinalcompose.RepositorioUsuarios
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val repo = RepositorioUsuarios()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isGoogleLogin = MutableStateFlow(false)
    val isGoogleLogin: StateFlow<Boolean> = _isGoogleLogin

    val isUserLoggedIn: Boolean
        get() = auth.currentUser != null

    fun registrarUsuarioAdmin(
        email: String,
        password: String,
        nombre: String,
        rol: Int,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                repo.registrarUsuarioAdmin(email, password, nombre, rol)
                onSuccess()
            } catch (e: Exception) {
                onError()
            }
        }
    }

    fun loginWithEmail(email: String, password: String) {

        _isLoading.value = true
        _errorMessage.value = null
        _loginSuccess.value = false

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val user = FirebaseAuth.getInstance().currentUser

                    if (user != null) {

                        val db = FirebaseFirestore.getInstance()
                        db.collection("usuarios")
                            .document(user.uid)
                            .get()
                            .addOnSuccessListener { document ->

                                if (document.exists()) {

                                    val usuario = document.toObject(Usuario::class.java)

                                    if (usuario != null) {
                                        DatosCompartidos.usuario = usuario
                                        _loginSuccess.value = true
                                    } else {
                                        _errorMessage.value = "Error al parsear usuario"
                                    }

                                } else {
                                    _errorMessage.value = "Usuario no existe en Firestore"
                                }

                                _isLoading.value = false
                            }
                            .addOnFailureListener {
                                _errorMessage.value = "Error al obtener usuario"
                                _isLoading.value = false
                            }

                    } else {
                        _errorMessage.value = "Usuario null"
                        _isLoading.value = false
                    }

                } else {
                    _errorMessage.value = "Error en login"
                    _isLoading.value = false
                }
            }
    }

    fun registerWithEmail(email: String, password: String, nombre: String, fotoUrl: String, rol: Int) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                repo.registrarUsuario(email, password, nombre, rol)
                _isLoading.value = false
                _isGoogleLogin.value = false
                _loginSuccess.value = true
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = e.message
            }
        }
    }

    fun loginWithGoogle(idToken: String) {

        if (idToken.isEmpty()) return

        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val user = auth.currentUser

                    if (user != null) {

                        viewModelScope.launch {
                            repo.registraGmailAutentificado(
                                uid = user.uid,
                                nombre = user.displayName ?: "",
                                email = user.email ?: "",
                                rol = 4
                            )
                        }

                        DatosCompartidos.usuario = Usuario(
                            id = user.uid,
                            nombre = user.displayName ?: "",
                            email = user.email ?: "",
                            rol = 4
                        )

                        _isGoogleLogin.value = true
                        _loginSuccess.value = true

                    } else {
                        _errorMessage.value = "Error al obtener usuario de Google"
                    }

                } else {
                    _errorMessage.value = "Error en login con Google"
                }
            }
    }

    fun signOut(context: Context) {
        auth.signOut()

        if (_isGoogleLogin.value) {
            val googleSignInClient = GoogleSignIn.getClient(
                context,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            )

            googleSignInClient.signOut()
            googleSignInClient.revokeAccess()
        }

        _loginSuccess.value = false
        _errorMessage.value = null
        _isLoading.value = false
        _isGoogleLogin.value = false
        DatosCompartidos.usuario = null
    }
}