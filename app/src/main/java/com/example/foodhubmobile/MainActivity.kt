package com.example.foodhubmobile

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.foodhubmobile.ui.screens.LoginActivity
import com.example.foodhubmobile.utils.SessionManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val session = SessionManager(this)
        val today = getToday()
        val deliveryPersonId = session.getDeliveryPersonId()

        android.util.Log.d("DEBUG_MAIN", "DeliveryPersonId: $deliveryPersonId")
        android.util.Log.d("DEBUG_MAIN", "Attendance today? ${session.isAttendanceMarkedToday(today)}")

        // ✅ FLOW LOGIC

        // 1️⃣ If user not logged in → go to LoginActivity
        if (deliveryPersonId.isNullOrEmpty()) {
            android.util.Log.d("DEBUG_MAIN", "No deliveryPersonId → go to LoginActivity")
            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
            return
        }

        // 2️⃣ If attendance not marked today → go to AttendanceActivity
        if (!session.isAttendanceMarkedToday(today)) {
            android.util.Log.d("DEBUG_MAIN", "Attendance not marked → go to AttendanceActivity")
            startActivity(Intent(this, AttendanceActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
            return
        }

        // 3️⃣ Everything OK → show app content
        android.util.Log.d("DEBUG_MAIN", "All checks passed → load FoodHubNavHost")
        try {
            setContent {
                FoodHubNavHost()
            }
        } catch (e: Exception) {
            android.util.Log.e("DEBUG_MAIN", "Error in setContent: ${e.message}", e)
        }
    }

    private fun getToday(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date())
    }
}


//package com.example.foodhubmobile
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import com.example.foodhubmobile.ui.screens.LoginActivity
//import com.example.foodhubmobile.utils.SessionManager
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//
//class MainActivity : ComponentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//
//        val session = SessionManager(this)
//        val today = getToday()
//
//
//        android.util.Log.d("DEBUG_MAIN", "Token: ${session.getToken()}")
//        android.util.Log.d("DEBUG_MAIN", "Attendance today? ${session.isAttendanceMarkedToday(today)}")
//
//
//        // ✅ FLOW LOGIC
//        if (session.getToken() == null) {
//            android.util.Log.d("DEBUG_MAIN", "No token → go to LoginActivity")
//            // Not logged in → go to Login
//            startActivity(Intent(this, LoginActivity::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            })
//            finish()
//            return
//        }
//
//        if (!session.isAttendanceMarkedToday(today)) {
//            android.util.Log.d("DEBUG_MAIN", "Attendance not marked → go to AttendanceActivity")
//
//            // Attendance not marked today → go to Attendance
//            startActivity(Intent(this, AttendanceActivity::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            })
//            finish()
//            return
//        }
//        android.util.Log.d("DEBUG_MAIN", "All checks passed → load FoodHubNavHost")
//        try {
//        // Everything OK → show app content
//        setContent {
//            FoodHubNavHost()
//        }
//        } catch (e: Exception) {
//            android.util.Log.e("DEBUG_MAIN", "Error in setContent: ${e.message}", e)
//        }
//    }
//
//    private fun getToday(): String {
//        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//            .format(Date())
//    }
//}

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
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import com.example.foodhubmobile.ui.screens.LoginActivity
//import com.example.foodhubmobile.ui.theme.FoodHubMobileTheme
//import com.example.foodhubmobile.utils.SessionManager
//import kotlinx.coroutines.delay
//import java.text.SimpleDateFormat
//import java.util.Calendar
//import java.util.Locale
//import kotlin.jvm.java
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//
//        setContent {
//            FoodHubMobileTheme {
//                RootContent()
//            }
//        }
//    }
//}
//
//@Composable
//fun RootContent() {
//    val context = LocalContext.current
//    val session = SessionManager(context)
//    val token = session.getToken()
//
//    // Safe for API 24+
//    val today = remember {
//        val calendar = Calendar.getInstance()
//        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//        sdf.format(calendar.time)
//    }
//
//    // Navigation side-effects
//    LaunchedEffect(token, today) {
//        when {
//            token == null -> {
//                context.startActivity(Intent(context, LoginActivity::class.java))
//            }
//            !session.isAttendanceMarkedToday(today) -> {
//                context.startActivity(Intent(context, AttendanceActivity::class.java))
//            }
//            else -> {
//                // Already logged in and attendance marked, proceed to your main app flow
//                // This could stay in MainActivity if using NavHost or Compose screens
//            }
//        }
//    }
//
//    // Optional UI: Show a loading / splash while navigating
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Yellow),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = "FOODHUB",
//            style = MaterialTheme.typography.headlineMedium,
//            color = Color.Black
//        )
//    }
//}
//
//@Composable
//fun AttendanceActivityLauncher() {
//    val context = LocalContext.current
//
//    LaunchedEffect(Unit) {
//        context.startActivity(
//            Intent(context, AttendanceActivity::class.java)
//        )
//    }
//}
//
////fun RootContent() {
////    val session = SessionManager(LocalContext.current)
////    val tokenExists = session.getToken() != null
////
////    if (tokenExists) {
////        // User already logged in → go to main app screen
////        FoodHubNavHost()
////    } else {
////        SplashAndLogin()
////    }
////}
//
//@Composable
//fun SplashAndLogin() {
//    var showWelcome by remember { mutableStateOf(true) }
//    val context = LocalContext.current
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Yellow),
//        contentAlignment = Alignment.Center
//    ) {
//        if (showWelcome) {
//            WelcomeScreen {
//                showWelcome = false
//                context.startActivity(Intent(context, LoginActivity::class.java))
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
//
//
////package com.example.foodhubmobile
////
////import android.content.Intent
////import android.os.Bundle
////import androidx.activity.ComponentActivity
////import androidx.activity.compose.setContent
////import androidx.activity.enableEdgeToEdge
////import androidx.compose.foundation.background
////import androidx.compose.foundation.layout.*
////import androidx.compose.material3.*
////import androidx.compose.runtime.*
////import androidx.compose.ui.Alignment
////import androidx.compose.ui.Modifier
////import androidx.compose.ui.graphics.Color
////import androidx.compose.ui.unit.dp
////import com.example.foodhubmobile.ui.screens.LoginActivity
////import com.example.foodhubmobile.ui.theme.FoodHubMobileTheme
////import com.example.foodhubmobile.utils.SessionManager
////import kotlinx.coroutines.delay
////
////class MainActivity : ComponentActivity() {
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        enableEdgeToEdge()
////
////        setContent {
////            FoodHubMobileTheme {
////
////                val session = SessionManager(this)
////                val tokenExists = session.getToken() != null
////
////                // If token exists → skip login
////                if (tokenExists) {
////                    FoodHubNavHost()
////                } else {
////                    var showWelcome by remember { mutableStateOf(true) }
////
////                    Box(
////                        modifier = Modifier
////                            .fillMaxSize()
////                            .background(Color.Yellow),
////                        contentAlignment = Alignment.Center
////                    ) {
////                        if (showWelcome) {
////                            WelcomeScreen {
////                                showWelcome = false
////                                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
////                                finish()
////                            }
////                        }
////                    }
////                }
////            }
////        }
////    }
////}
////
////@Composable
////fun WelcomeScreen(onTimeout: () -> Unit) {
////    LaunchedEffect(Unit) {
////        delay(2000) // 2 seconds splash
////        onTimeout()
////    }
////
////    Text(
////        text = "FOODHUB",
////        style = MaterialTheme.typography.headlineMedium,
////        color = Color.Black
////    )
////}
