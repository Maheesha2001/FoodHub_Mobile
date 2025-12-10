package com.example.foodhubmobile

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.foodhubmobile.ui.screens.LoginActivity
import com.example.foodhubmobile.ui.theme.FoodHubMobileTheme
import com.example.foodhubmobile.utils.SessionManager
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FoodHubMobileTheme {
                RootContent()
            }
        }
    }
}

@Composable
fun RootContent() {
    val session = SessionManager(LocalContext.current)
    val tokenExists = session.getToken() != null

    if (tokenExists) {
        // User already logged in → go to main app screen
        FoodHubNavHost()
    } else {
        SplashAndLogin()
    }
}

@Composable
fun SplashAndLogin() {
    var showWelcome by remember { mutableStateOf(true) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow),
        contentAlignment = Alignment.Center
    ) {
        if (showWelcome) {
            WelcomeScreen {
                showWelcome = false
                context.startActivity(Intent(context, LoginActivity::class.java))
            }
        }
    }
}

@Composable
fun WelcomeScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000) // 2 seconds splash
        onTimeout()
    }

    Text(
        text = "FOODHUB",
        style = MaterialTheme.typography.headlineMedium,
        color = Color.Black
    )
}


//package com.example.foodhubmobile
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import com.example.foodhubmobile.ui.screens.LoginActivity
//import com.example.foodhubmobile.ui.theme.FoodHubMobileTheme
//import com.example.foodhubmobile.utils.SessionManager
//import kotlinx.coroutines.delay
//
//class MainActivity : ComponentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//
//        setContent {
//            FoodHubMobileTheme {
//
//                val session = SessionManager(this)
//                val tokenExists = session.getToken() != null
//
//                // If token exists → skip login
//                if (tokenExists) {
//                    FoodHubNavHost()
//                } else {
//                    var showWelcome by remember { mutableStateOf(true) }
//
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .background(Color.Yellow),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        if (showWelcome) {
//                            WelcomeScreen {
//                                showWelcome = false
//                                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
//                                finish()
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun WelcomeScreen(onTimeout: () -> Unit) {
//    LaunchedEffect(Unit) {
//        delay(2000) // 2 seconds splash
//        onTimeout()
//    }
//
//    Text(
//        text = "FOODHUB",
//        style = MaterialTheme.typography.headlineMedium,
//        color = Color.Black
//    )
//}
