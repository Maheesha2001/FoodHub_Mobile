    package com.example.foodhubmobile.ui.screens

    import android.util.Log
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.material3.*
    import androidx.compose.runtime.*
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.unit.dp
    import com.example.foodhubmobile.models.Order
    import com.example.foodhubmobile.network.ApiClient
    import com.example.foodhubmobile.network.ApiService
    import kotlinx.coroutines.launch

    @Composable
    fun PendingOrdersScreen(
        onSelectOrder: (Order) -> Unit = {}
    ) {
        val api = ApiClient.retrofit.create(ApiService::class.java)
        var orders by remember { mutableStateOf<List<Order>>(emptyList()) }
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            try {
                val response = api.getPendingOrders()
                if (response.isSuccessful) {
                    // Update orders list
                    orders = response.body() ?: emptyList()
                    Log.d("PendingOrders", "Loaded ${orders.size} orders")
                } else {
                    Log.e("PendingOrders", "Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("PendingOrders", "Exception fetching orders", e)
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text("Pending Deliveries list", style = MaterialTheme.typography.titleLarge)

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(orders) { order ->
                    OrderCard(order) { onSelectOrder(order) }
                }
            }
        }
    }

    @Composable
    fun OrderCard(order: Order, onClick: () -> Unit) {
        Card(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .clickable { onClick() }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Order: ${order.id}", style = MaterialTheme.typography.titleMedium)
                order.items.forEach { item ->
                    Text("${item.productName} x${item.quantity} = ${item.totalPrice}")
                }
            }
        }
    }
