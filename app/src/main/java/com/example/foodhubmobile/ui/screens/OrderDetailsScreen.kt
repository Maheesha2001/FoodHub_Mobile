package com.example.foodhubmobile.ui.screens

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
fun OrderDetailsScreen(
    orderCode: String,
    onComplete: () -> Unit
) {
    val api = ApiClient.retrofit.create(ApiService::class.java)
    var order by remember { mutableStateOf<Order?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(orderCode) {
        val response = api.getOrderByCode(orderCode)
        if (response.isSuccessful) {
            order = response.body()
        }
    }

    if (order == null) {
        CircularProgressIndicator()
        return
    }

    Column(modifier = Modifier.padding(20.dp)) {

        Text(
            text = "Order Code: ${order!!.code}",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Total Amount: ${order!!.totalAmount}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Items", style = MaterialTheme.typography.titleMedium)

        LazyColumn {
            items(order!!.items) { item ->
                Text("${item.productName} x${item.quantity} = ${item.totalPrice}")
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                scope.launch {
                    val response = api.markDelivered(order!!.code)
                    if (response.isSuccessful) {
                        onComplete()
                    }
                }
            }
        ) {
            Text("Mark Delivered")
        }
    }
}


//
//@Composable
//fun OrderDetailsScreen(order: Order, onComplete: () -> Unit) {
//    val api = ApiClient.retrofit.create(ApiService::class.java)
//    val scope = rememberCoroutineScope()
//
//    Column(modifier = Modifier.padding(20.dp)) {
//        Text("Order Code: ${order.code}", style = MaterialTheme.typography.titleLarge)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text("Total Amount: \$${order.totalAmount}", style = MaterialTheme.typography.titleMedium)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text("Items:", style = MaterialTheme.typography.titleMedium)
//        Spacer(modifier = Modifier.height(8.dp))
//
//        LazyColumn(modifier = Modifier.fillMaxWidth()) {
//            items(order.items) { item ->
//                Text("${item.productName} x${item.quantity} - \$${item.totalPrice}")
//                Spacer(modifier = Modifier.height(4.dp))
//            }
//        }
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        Button(onClick = {
//            scope.launch {
//                // Using order.code if your backend expects code instead of id
//                val orderIdentifier = order.code ?: order.id.toString()
//                val response = api.markDelivered(orderIdentifier, true)
//                if (response.isSuccessful) onComplete()
//            }
//        }) {
//            Text("Mark Delivered")
//        }
//    }
//}
//
//
//
