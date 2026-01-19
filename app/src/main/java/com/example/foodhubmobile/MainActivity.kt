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
import com.example.foodhubmobile.ui.screens.SplashScreen


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SplashScreen()
        }

        val session = SessionManager(this)
        val today = getToday()
        val deliveryPersonId = session.getDeliveryPersonId()

        android.util.Log.d("DEBUG_MAIN", "DeliveryPersonId: $deliveryPersonId")
        android.util.Log.d("DEBUG_MAIN", "Attendance today? ${session.isAttendanceMarkedToday(today)}")

        // ✅ FLOW LOGIC
        android.os.Handler(mainLooper).postDelayed({
        // 1️⃣ If user not logged in → go to LoginActivity
        if (deliveryPersonId.isNullOrEmpty()) {
            android.util.Log.d("DEBUG_MAIN", "No deliveryPersonId → go to LoginActivity")
            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
            //return
            return@postDelayed
        }

        // 2️⃣ If attendance not marked today → go to AttendanceActivity
        if (!session.isAttendanceMarkedToday(today)) {
            android.util.Log.d("DEBUG_MAIN", "Attendance not marked → go to AttendanceActivity")
            startActivity(Intent(this, AttendanceActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
            //return
            return@postDelayed
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
        }, 1500)
    }


    private fun getToday(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date())
    }
}