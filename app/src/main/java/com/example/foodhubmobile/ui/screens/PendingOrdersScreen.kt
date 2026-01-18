        package com.example.foodhubmobile.ui.screens
    
        import android.util.Log
        import androidx.compose.foundation.clickable
        import androidx.compose.foundation.layout.*
        import androidx.compose.foundation.lazy.LazyColumn
        import androidx.compose.foundation.lazy.items
        import androidx.compose.material3.*
        import androidx.compose.runtime.*
        import androidx.compose.ui.Modifier
        import androidx.compose.ui.text.SpanStyle
        import androidx.compose.ui.text.buildAnnotatedString
        import androidx.compose.ui.text.font.FontWeight
        import androidx.compose.ui.text.withStyle
        import androidx.compose.ui.unit.dp
        import com.example.foodhubmobile.models.PendingOrder
        import com.example.foodhubmobile.network.ApiClient
        import com.example.foodhubmobile.network.ApiService
        import kotlinx.coroutines.launch

        @Composable
        fun PendingOrdersScreen(
            onSelectOrder: (PendingOrder) -> Unit = {}
        ) {
            val api = ApiClient.retrofit.create(ApiService::class.java)
            var orders by remember { mutableStateOf<List<PendingOrder>>(emptyList()) }

            LaunchedEffect(Unit) {
                try {
                    val response = api.getPendingOrders()
                    if (response.isSuccessful) {
                        orders = response.body() ?: emptyList()
                        Log.d("PendingOrders", "Loaded ${orders.size} orders")
                    } else {
                        Log.e("PendingOrders", "Error ${response.code()}")
                    }
                } catch (e: Exception) {
                    Log.e("PendingOrders", "Exception", e)
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Pending Deliveries",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    items(orders) { order ->
                        PendingOrderCard(order) {
                            onSelectOrder(order)
                        }
                    }
                }
            }
        }

        @Composable
        fun PendingOrderCard(
            order: PendingOrder,
            onClick: () -> Unit
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onClick() },
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Order Code: ")
                            }
                            append(order.code)
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )


                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Customer: ")
                            }
                            append(order.userName)
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )


                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Delivery Name: ")
                            }
                            append(order.dilveryName)
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Delivery Address: ")
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = order.address,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
