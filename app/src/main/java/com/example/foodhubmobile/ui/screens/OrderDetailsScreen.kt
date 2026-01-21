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

    val darkBackground = Color(0xFF212529)
    val accent = Color(0xFFFFC107)
    val darkText = Color(0xFF212529)

    LaunchedEffect(orderCode) {
        val response = api.getOrderByCode(orderCode)
        if (response.isSuccessful) {
            order = response.body()
        }
        isLoading = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
            .systemBarsPadding()      // ✅ top safe area
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 32.dp       // ✅ bottom spacing
            )
    ) {

        // 🔄 Loading State
        if (isLoading) {
            CircularProgressIndicator(
                color = accent,
                modifier = Modifier.align(Alignment.Center)
            )
            return@Box
        }

        Column {

            // Header Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = "Order #${order!!.code}",
                        style = MaterialTheme.typography.titleLarge,
                        color = accent,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Total Amount",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )

                    Text(
                        text = "Rs. ${order!!.totalAmount}",
                        style = MaterialTheme.typography.titleMedium,
                        color = darkText,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Items Title
            Text(
                text = "Items",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Items Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                LazyColumn(
                    modifier = Modifier.padding(16.dp)
                ) {
                    items(order!!.items) { item ->
                        Text(
                            text = "${item.productName} x${item.quantity}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = darkText
                        )

                        Text(
                            text = "Rs. ${item.totalPrice}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // pushes button down

            // Action Button
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accent,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(14.dp),
                onClick = {
                    scope.launch {
                        val response = api.markDelivered(order!!.code)
                        if (response.isSuccessful) {
                            onComplete()
                        }
                    }
                }
            ) {
                Text(
                    text = "Mark Delivered",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

