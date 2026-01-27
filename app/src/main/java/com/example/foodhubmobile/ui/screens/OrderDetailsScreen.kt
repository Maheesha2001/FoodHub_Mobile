//package com.example.foodhubmobile.ui.screens
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import com.example.foodhubmobile.models.Order
//import com.example.foodhubmobile.network.ApiClient
//import com.example.foodhubmobile.network.ApiService
//import kotlinx.coroutines.launch
//
//
//@Composable
//fun OrderDetailsScreen(
//    orderCode: String,
//    onComplete: () -> Unit
//) {
//    val api = ApiClient.retrofit.create(ApiService::class.java)
//    var order by remember { mutableStateOf<Order?>(null) }
//    var isLoading by remember { mutableStateOf(true) }
//    val scope = rememberCoroutineScope()
//
//    val darkBackground = Color(0xFF212529)
//    val accent = Color(0xFFFFC107)
//    val darkText = Color(0xFF212529)
//
//    LaunchedEffect(orderCode) {
//        val response = api.getOrderByCode(orderCode)
//        if (response.isSuccessful) {
//            order = response.body()
//        }
//        isLoading = false
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(darkBackground)
//            .systemBarsPadding()
//            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp)
//    ) {
//
//        if (isLoading) {
//            CircularProgressIndicator(
//                color = accent,
//                modifier = Modifier.align(Alignment.Center)
//            )
//            return@Box
//        }
//
//        Column {
//
//            // Header Card
//            Card(
//                shape = RoundedCornerShape(16.dp),
//                colors = CardDefaults.cardColors(containerColor = Color.White),
//                elevation = CardDefaults.cardElevation(6.dp),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Column(modifier = Modifier.padding(16.dp)) {
//
//                    Text(
//                        text = "Order #${order!!.code}",
//                        style = MaterialTheme.typography.titleLarge,
//                        color = accent,
//                        fontWeight = FontWeight.Bold
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Text(
//                        text = "Total Amount",
//                        style = MaterialTheme.typography.labelMedium,
//                        color = Color.Gray
//                    )
//
//                    Text(
//                        text = "Rs. ${order!!.totalAmount}",
//                        style = MaterialTheme.typography.titleMedium,
//                        color = darkText,
//                        fontWeight = FontWeight.SemiBold
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    // ✅ Payment Status
//                    Text(
//                        text = "Payment Status",
//                        style = MaterialTheme.typography.labelMedium,
//                        color = Color.Gray
//                    )
//
//                    val paymentColor = when (order!!.paymentStatus?.lowercase()) {
//                        "paid" -> Color(0xFF198754)           // Green
//                        "pending" -> Color(0xFFFFC107)        // Yellow
//                        "failed" -> Color(0xFFDC3545)         // Red
//                        else -> Color.Gray
//
//                    }
//
//                    Text(
//                        text = order!!.paymentStatus ?: "Not Available",
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = paymentColor,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Items Title
//            Text(
//                text = "Items",
//                style = MaterialTheme.typography.titleMedium,
//                color = Color.White
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Items Card
//            Card(
//                shape = RoundedCornerShape(16.dp),
//                colors = CardDefaults.cardColors(containerColor = Color.White),
//                elevation = CardDefaults.cardElevation(4.dp),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                LazyColumn(
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    items(order!!.items) { item ->
//                        Text(
//                            text = "${item.productName} x${item.quantity}",
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = darkText
//                        )
//
//                        Text(
//                            text = "Rs. ${item.totalPrice}",
//                            style = MaterialTheme.typography.bodySmall,
//                            color = Color.Gray
//                        )
//
//                        Spacer(modifier = Modifier.height(8.dp))
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            // Action Button
//            Button(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(52.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = accent,
//                    contentColor = Color.Black
//                ),
//                shape = RoundedCornerShape(14.dp),
//                onClick = {
//                    scope.launch {
//                        val response = api.markDelivered(order!!.code)
//                        if (response.isSuccessful) {
//                            onComplete()
//                        }
//                    }
//                }
//            ) {
//                Text(
//                    text = "Mark Delivered",
//                    fontWeight = FontWeight.Bold
//                )
//            }
//        }
//    }
//}
//
//

package com.example.foodhubmobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.foodhubmobile.models.Order
import com.example.foodhubmobile.network.ApiClient
import com.example.foodhubmobile.network.ApiService
import kotlinx.coroutines.launch

@Composable
fun OrderDetailsScreen(
    orderCode: String,
    onComplete: () -> Unit
) {
    val api = ApiClient.retrofit.create(ApiService::class.java)
    var order by remember { mutableStateOf<Order?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    // 🎨 Colors
    val bgColor = Color(0xFFF8F9FA)
    val cardColor = Color.White
    val primary = Color(0xFFFFC107)
    val textPrimary = Color(0xFF212529)
    val textSecondary = Color(0xFF6C757D)

    LaunchedEffect(orderCode) {
        val response = api.getOrderByCode(orderCode)
        if (response.isSuccessful) {
            order = response.body()
        }
        isLoading = false
    }

    Scaffold(
        containerColor = bgColor,
        bottomBar = {
            if (!isLoading && order != null) {
                Button(
                    onClick = {
                        scope.launch {
                            val response = api.markDelivered(order!!.code)
                            if (response.isSuccessful) {
                                onComplete()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primary,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Mark as Delivered",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    ) { paddingValues ->

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = primary)
            }
            return@Scaffold
        }

        order?.let { data ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // 🔹 ORDER SUMMARY CARD
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = cardColor),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {

                            Text(
                                text = "Order #${data.code}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Total Amount",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = textSecondary
                                    )
                                    Text(
                                        text = "Rs. ${data.totalAmount}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                PaymentStatusChip(status = data.paymentStatus)
                            }
                        }
                    }
                }

                // 🔹 ITEMS HEADER
                item {
                    Text(
                        text = "Ordered Items",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = textPrimary
                    )
                }

                // 🔹 ITEMS LIST
                items(data.items) { item ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = cardColor),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = item.productName,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium,
                                    color = textPrimary
                                )
                                Text(
                                    text = "Qty: ${item.quantity}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = textSecondary
                                )
                            }

                            Text(
                                text = "Rs. ${item.totalPrice}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun PaymentStatusChip(status: String?) {
    val (bg, text) = when (status?.lowercase()) {
        "paid" -> Color(0xFFE6F4EA) to Color(0xFF198754)
        "pending" -> Color(0xFFFFF3CD) to Color(0xFFFFC107)
        "failed" -> Color(0xFFF8D7DA) to Color(0xFFDC3545)
        else -> Color(0xFFE9ECEF) to Color.Gray
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = bg
    ) {
        Text(
            text = status ?: "Unknown",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = text,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
