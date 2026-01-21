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
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
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

}

//@Composable
//fun AttendanceScreen(date: String, onMarkAttendance: () -> Unit) {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            Text("Attendance for $date", style = MaterialTheme.typography.titleLarge)
//            Spacer(modifier = Modifier.height(20.dp))
//            Button(onClick = onMarkAttendance) {
//                Text("Mark Attendance")
//            }
//        }
//    }
//}

@Composable
fun AttendanceScreen(
    date: String,
    onMarkAttendance: () -> Unit
) {
    val primaryYellow = Color(0xFFFFC107)
    val darkBackground = Color(0xFF212529)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Today's Attendance",
                    style = MaterialTheme.typography.titleLarge,
                    color = darkBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = date,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onMarkAttendance,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryYellow,
                        contentColor = darkBackground
                    )
                ) {
                    Text(
                        text = "Mark Attendance",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

