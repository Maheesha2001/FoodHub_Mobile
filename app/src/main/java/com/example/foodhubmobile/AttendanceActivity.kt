package com.example.foodhubmobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.foodhubmobile.network.RetrofitClient
import com.example.foodhubmobile.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AttendanceActivity : ComponentActivity() {

    private lateinit var session: SessionManager
    private val today: String by lazy {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        session = SessionManager(this)

        setContent {
            AttendanceScreen(date = today, onMarkAttendance = { markAttendance() })
        }
    }

//    private fun markAttendance() {
//        val token = session.getToken() ?: ""
//        if (token.isEmpty()) {
//            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        RetrofitClient.instance.markAttendance(token, today)
//            .enqueue(object : Callback<Unit> {
//                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
//                    if (response.isSuccessful) {
//                        session.saveAttendanceMarked(today)
//                        Toast.makeText(this@AttendanceActivity, "Attendance marked!", Toast.LENGTH_SHORT).show()
//                        startActivity(Intent(this@AttendanceActivity, MainActivity::class.java).apply {
//                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        })
//                        finish()
//                    } else {
//                        Toast.makeText(this@AttendanceActivity, "Failed to mark attendance: ${response.code()}", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<Unit>, t: Throwable) {
//                    Toast.makeText(this@AttendanceActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
//                }
//            })
//    }

    private fun markAttendance() {
        val deliveryPersonId = session.getDeliveryPersonId()
        Log.d("DEBUG_ATTENDANCE", "DeliveryPersonId: $deliveryPersonId, Date: $today")
        if (deliveryPersonId.isNullOrEmpty()) {
            Toast.makeText(this, "Delivery person ID not found. Login again.", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("DEBUG_ATTENDANCE", "DeliveryPersonId: $deliveryPersonId")
        Log.d("DEBUG_ATTENDANCE", "Date: $today")

        RetrofitClient.instance.markAttendance(deliveryPersonId, today)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    Log.d("DEBUG_ATTENDANCE", "Response code: ${response.code()}")
                    Log.d("DEBUG_ATTENDANCE", "Response body: ${response.body()}")
                    Log.d("DEBUG_ATTENDANCE", "Response errorBody: ${response.errorBody()?.string()}")

                    if (response.isSuccessful) {
                        session.saveAttendanceMarked(today)
                        Toast.makeText(this@AttendanceActivity, "Attendance marked!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@AttendanceActivity, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        })
                        finish()
                    } else {
                        Toast.makeText(
                            this@AttendanceActivity,
                            "Failed to mark attendance: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.e("DEBUG_ATTENDANCE", "Network failure", t)
                    Toast.makeText(this@AttendanceActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

//    private fun markAttendance() {
//    val token = session.getToken() ?: ""
//    if (token.isEmpty()) {
//        Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show()
//        return
//    }
//
//    // Log the token and date
//    Log.d("DEBUG_ATTENDANCE", "Token: $token")
//    Log.d("DEBUG_ATTENDANCE", "Date: $today")
//
//    // If you pass DeliveryPersonId instead of token, log it too
//    val deliveryPersonId = session.getDeliveryPersonId() ?: "UNKNOWN"
//    Log.d("DEBUG_ATTENDANCE", "DeliveryPersonId: $deliveryPersonId")
//
//    // Make API call
//    val call = RetrofitClient.instance.markAttendance(token, today)
//
//    // Log the actual Retrofit call (URL will be built by Retrofit)
//    Log.d("DEBUG_ATTENDANCE", "Retrofit call: $call")
//
//    call.enqueue(object : Callback<Unit> {
//        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
//            Log.d("DEBUG_ATTENDANCE", "Response code: ${response.code()}")
//            Log.d("DEBUG_ATTENDANCE", "Response body: ${response.body()}")
//            Log.d("DEBUG_ATTENDANCE", "Response errorBody: ${response.errorBody()?.string()}")
//
//            if (response.isSuccessful) {
//                session.saveAttendanceMarked(today)
//                Toast.makeText(this@AttendanceActivity, "Attendance marked!", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this@AttendanceActivity, MainActivity::class.java).apply {
//                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                })
//                finish()
//            } else {
//                Toast.makeText(
//                    this@AttendanceActivity,
//                    "Failed to mark attendance: ${response.code()}",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//
//        override fun onFailure(call: Call<Unit>, t: Throwable) {
//            Log.e("DEBUG_ATTENDANCE", "Network failure", t)
//            Toast.makeText(this@AttendanceActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
//        }
//    })
//}

}

@Composable
fun AttendanceScreen(date: String, onMarkAttendance: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Attendance for $date", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = onMarkAttendance) {
                Text("Mark Attendance")
            }
        }
    }
}


//package com.example.foodhubmobile
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import com.example.foodhubmobile.utils.SessionManager
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//
//class AttendanceActivity : ComponentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        val session = SessionManager(this)
//        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
//
//        android.util.Log.d("DEBUG_ATTENDANCE", "Marking attendance for $today")
//
//        // ✅ Mark attendance
//        session.saveAttendanceMarked(today)
//        android.util.Log.d("DEBUG_ATTENDANCE", "Starting MainActivity")
//        // ✅ Go back to MainActivity with cleared stack
//        startActivity(
//            Intent(this, MainActivity::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            }
//        )
//        finish()
//    }
//}
//
//
////package com.example.foodhubmobile
////
////import android.content.Intent
////import android.os.Bundle
////import androidx.activity.ComponentActivity
////import androidx.activity.compose.setContent
////import com.example.foodhubmobile.ui.screens.AttendanceScreen
////import com.example.foodhubmobile.ui.screens.LoginActivity
////import com.example.foodhubmobile.ui.theme.FoodHubMobileTheme
////import com.example.foodhubmobile.utils.SessionManager
////
////class AttendanceActivity : ComponentActivity() {
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////
////        val session = SessionManager(this)
////
////        setContent {
////            FoodHubMobileTheme {
////                AttendanceScreen(
////                    onMarkPresent = {
////                        // Save attendance locally (for now)
////                        session.saveAttendanceMarked()
////
////                        // Go back to Login page
////                        val intent = Intent(this, LoginActivity::class.java)
////                        intent.flags =
////                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
////                        startActivity(intent)
////                        finish()
////                    }
////                )
////            }
////        }
////    }
////}
////
//////package com.example.foodhubmobile
//////
//////import android.content.Intent
//////import android.os.Bundle
//////import androidx.activity.ComponentActivity
//////import androidx.activity.compose.setContent
//////import androidx.compose.foundation.layout.Arrangement
//////import androidx.compose.foundation.layout.Column
//////import androidx.compose.foundation.layout.Spacer
//////import androidx.compose.foundation.layout.fillMaxSize
//////import androidx.compose.foundation.layout.fillMaxWidth
//////import androidx.compose.foundation.layout.height
//////import androidx.compose.foundation.layout.padding
//////import androidx.compose.material3.Button
//////import androidx.compose.material3.MaterialTheme
//////import androidx.compose.material3.Text
//////import androidx.compose.runtime.Composable
//////import androidx.compose.ui.Modifier
//////import androidx.compose.ui.unit.dp
//////import com.example.foodhubmobile.utils.SessionManager
//////import java.text.SimpleDateFormat
//////import java.util.Calendar
//////import java.util.Locale
//////
//////class AttendanceActivity : ComponentActivity() {
//////
//////    private lateinit var session: SessionManager
//////
//////    override fun onCreate(savedInstanceState: Bundle?) {
//////        super.onCreate(savedInstanceState)
//////
//////        session = SessionManager(this)
//////
//////        setContent {
//////            AttendanceScreen(
//////                onMarkPresent = {
//////                    markAttendance()
//////                }
//////            )
//////        }
//////    }
//////
//////    private fun markAttendance() {
//////        // 🔹 Later you can call API here (POST /attendance/checkin)
//////
//////        // Get today's date in yyyy-MM-dd format
//////        val calendar = Calendar.getInstance()
//////        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//////        val today = sdf.format(calendar.time)
//////
//////        // Save attendance locally
//////        session.saveAttendanceMarked(today)
//////
//////        // Go back to Main flow
//////        startActivity(Intent(this, MainActivity::class.java))
//////        finish()
//////    }
//////}
//////
//////@Composable
//////fun AttendanceScreen(onMarkPresent: () -> Unit) {
//////    Column(
//////        modifier = Modifier
//////            .fillMaxSize()
//////            .padding(24.dp),
//////        verticalArrangement = Arrangement.Center
//////    ) {
//////        Text("Mark Attendance", style = MaterialTheme.typography.headlineMedium)
//////        Spacer(Modifier.height(20.dp))
//////        Text("Please mark yourself present before starting deliveries.")
//////        Spacer(Modifier.height(30.dp))
//////        Button(
//////            onClick = onMarkPresent,
//////            modifier = Modifier.fillMaxWidth()
//////        ) {
//////            Text("Mark Present")
//////        }
//////    }
//////}
