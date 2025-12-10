package com.example.foodhubmobile.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.util.Log
import com.example.foodhubmobile.MainActivity
import com.example.foodhubmobile.models.LoginRequest
import com.example.foodhubmobile.models.LoginResponse
import com.example.foodhubmobile.network.RetrofitClient
import com.example.foodhubmobile.utils.SessionManager
import retrofit2.Call

class LoginActivity : FragmentActivity() {

    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        session = SessionManager(this)

        setContent {
            LoginScreen(
                onLogin = { email, password ->
                    login(email, password)
                },
                onBiometric = {
                    showBiometricPrompt()
                }
            )
        }
    }
//
//    private fun login(email: String, password: String) {
//        val call = RetrofitClient.instance.login(LoginRequest(email, password))
//
//        call.enqueue(object : retrofit2.Callback<LoginResponse> {
//            override fun onResponse(
//                call: Call<LoginResponse>,
//                response: retrofit2.Response<LoginResponse>
//            ) {
//                val res = response.body()
//                if (res != null && res.success) {
//                    session.saveToken(res.token!!)
//                    startHomeActivity()
//                } else {
//                    Toast.makeText(this@LoginActivity, "Invalid login", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                Toast.makeText(this@LoginActivity, "Server error", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }

    private fun login(email: String, password: String) {

        Log.d("LOGIN_DEBUG", "Attempt login => email=$email, password=$password")

        val call = RetrofitClient.instance.login(LoginRequest(email, password))

        call.enqueue(object : retrofit2.Callback<LoginResponse> {

            override fun onResponse(
                call: Call<LoginResponse>,
                response: retrofit2.Response<LoginResponse>
            ) {

                Log.d("LOGIN_DEBUG", "HTTP code: ${response.code()}")

                if (response.isSuccessful) {
                    val res = response.body()
                    Log.d("LOGIN_DEBUG", "Body: $res")

                    if (res != null && res.success) {
                        session.saveToken(res.token!!)
                        startHomeActivity()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Login failed: ${res?.message ?: "Wrong email or password"}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } else {
                    val errorText = response.errorBody()?.string()
                    Log.e("LOGIN_DEBUG", "Server error: $errorText")

                    Toast.makeText(
                        this@LoginActivity,
                        "Server error: $errorText",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LOGIN_DEBUG", "Retrofit failure", t)
                Toast.makeText(this@LoginActivity, "Network failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(this)

        val biometricPrompt = BiometricPrompt(
            this,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    session.saveToken("BIOMETRIC_LOGIN")
                    startHomeActivity()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    Toast.makeText(this@LoginActivity, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationFailed() {
                    Toast.makeText(this@LoginActivity, "Fingerprint not recognized", Toast.LENGTH_SHORT).show()
                }
            })

        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Login with Fingerprint")
            .setDescription("Use your fingerprint to login")
            .setNegativeButtonText("Cancel")
            .build()

        biometricPrompt.authenticate(info)
    }

    private fun startHomeActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

@Composable
fun LoginScreen(onLogin: (String, String) -> Unit, onBiometric: () -> Unit) {

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Column(Modifier.padding(20.dp)) {

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { onLogin(email.value, password.value) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onBiometric,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login with Fingerprint")
        }
    }
}
