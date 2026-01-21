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
        import androidx.compose.foundation.background
        import androidx.compose.foundation.shape.RoundedCornerShape
        import androidx.compose.ui.graphics.Color
        import com.example.foodhubmobile.utils.SessionManager

        //        @Composable
//        fun PendingOrdersScreen(
//            onSelectOrder: (PendingOrder) -> Unit = {}
//        ) {
//            val api = ApiClient.retrofit.create(ApiService::class.java)
//            var orders by remember { mutableStateOf<List<PendingOrder>>(emptyList()) }
//
//            LaunchedEffect(Unit) {
//                try {
//                    val response = api.getPendingOrders()
//                    if (response.isSuccessful) {
//                        orders = response.body() ?: emptyList()
//                        Log.d("PendingOrders", "Loaded ${orders.size} orders")
//                    } else {
//                        Log.e("PendingOrders", "Error ${response.code()}")
//                    }
//                } catch (e: Exception) {
//                    Log.e("PendingOrders", "Exception", e)
//                }
//            }
//
//            Column(modifier = Modifier.padding(16.dp)) {
//                Text(
//                    text = "Pending Deliveries",
//                    style = MaterialTheme.typography.titleLarge
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                LazyColumn {
//                    items(orders) { order ->
//                        PendingOrderCard(order) {
//                            onSelectOrder(order)
//                        }
//                    }
//                }
//            }
//        }

//        @Composable
//        fun PendingOrdersScreen(
//            onSelectOrder: (PendingOrder) -> Unit = {}
//        ) {
//            val api = ApiClient.retrofit.create(ApiService::class.java)
//            var orders by remember { mutableStateOf<List<PendingOrder>>(emptyList()) }
//
//            val primaryYellow = Color(0xFFFFC107)
//            val darkBackground = Color(0xFF212529)
//
//            LaunchedEffect(Unit) {
//                try {
//                    val response = api.getPendingOrders()
//                    if (response.isSuccessful) {
//                        orders = response.body() ?: emptyList()
//                        Log.d("PendingOrders", "Loaded ${orders.size} orders")
//                    } else {
//                        Log.e("PendingOrders", "Error ${response.code()}")
//                    }
//                } catch (e: Exception) {
//                    Log.e("PendingOrders", "Exception", e)
//                }
//            }
//
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(darkBackground)
//                    .systemBarsPadding()
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(
//                            start = 16.dp,
//                            end = 16.dp,
//                            top = 24.dp,
//                            bottom = 24.dp
//                        )
//                ) {
//
//                    Text(
//                        text = "Pending Deliveries",
//                        style = MaterialTheme.typography.titleLarge,
//                        color = Color.White
//                    )
//
//                    Spacer(modifier = Modifier.height(12.dp))
//
//                    LazyColumn(
//                        contentPadding = PaddingValues(bottom = 16.dp)
//                    ) {
//                        items(orders) { order ->
//                            PendingOrderCard(
//                                order = order,
//                                onClick = { onSelectOrder(order) }
//                            )
//                        }
//                    }
//                }
//            }
//
//        }

        @Composable
        fun PendingOrdersScreen(
            session: SessionManager,
            onSelectOrder: (PendingOrder) -> Unit = {}
        ) {
            val api = ApiClient.retrofit.create(ApiService::class.java)
            var orders by remember { mutableStateOf<List<PendingOrder>>(emptyList()) }

            val primaryYellow = Color(0xFFFFC107)
            val darkBackground = Color(0xFF212529)

            LaunchedEffect(Unit) {
                try {
                    val driverId = session.getDeliveryPersonId() // get driverId from session
                    if (!driverId.isNullOrEmpty()) {
                        val response = api.GetTodaysAssignedOrders(driverId)
                        if (response.isSuccessful) {
                            orders = response.body() ?: emptyList()
                            Log.d("PendingOrders", "Loaded ${orders.size} orders")
                        } else {
                            Log.e("PendingOrders", "Error ${response.code()}")
                        }
                    } else {
                        Log.e("PendingOrders", "Driver ID not found in session")
                    }
                } catch (e: Exception) {
                    Log.e("PendingOrders", "Exception", e)
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(darkBackground)
                    .systemBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 24.dp,
                            bottom = 24.dp
                        )
                ) {

                    Text(
                        text = "Pending Deliveries",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(orders) { order ->
                            PendingOrderCard(
                                order = order,
                                onClick = { onSelectOrder(order) }
                            )
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
            val darkText = Color(0xFF212529)
            val accent = Color(0xFFFFC107)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onClick() },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // Order Code (Highlight)
                    Text(
                        text = "Order #${order.orderCode}",
                        style = MaterialTheme.typography.titleMedium,
                        color = accent,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Customer",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = order.customerName ?: "N/A",
                        style = MaterialTheme.typography.bodyMedium,
                        color = darkText
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Delivery Name",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = order.dilveryName ?: "N/A",
                        style = MaterialTheme.typography.bodyMedium,
                        color = darkText
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Delivery Address",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = order.address ?: "N/A",
                        style = MaterialTheme.typography.bodyMedium,
                        color = darkText
                    )
                }
            }
        }
