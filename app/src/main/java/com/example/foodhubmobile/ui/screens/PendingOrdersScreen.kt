package com.example.foodhubmobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodhubmobile.models.PendingOrder
import com.example.foodhubmobile.network.ApiClient
import com.example.foodhubmobile.utils.MapUtils
import com.example.foodhubmobile.utils.SessionManager
import com.example.foodhubmobile.realtime.SignalRManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendingOrdersScreen(
    session: SessionManager,
    navController: NavController,
    onViewDetails: (PendingOrder) -> Unit,
    onCheckout: () -> Unit
) {
    val api = ApiClient.retrofit.create(com.example.foodhubmobile.network.ApiService::class.java)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var orders by remember { mutableStateOf<List<PendingOrder>>(emptyList()) }
    var showCheckoutDialog by remember { mutableStateOf(false) }

    val bgColor = Color(0xFFF8F9FA)
    val primary = Color(0xFFFFC107)
    val textPrimary = Color(0xFF212529)
    val textSecondary = Color(0xFF6C757D)

    // -------------------------------
    // Load orders from API
    // -------------------------------
    suspend fun loadOrders() {
        val driverId = session.getDeliveryPersonId()
        if (!driverId.isNullOrEmpty()) {
            val response = api.GetTodaysAssignedOrders(driverId)
            if (response.isSuccessful) {
                orders = response.body() ?: emptyList()
            }
        }
    }

    LaunchedEffect(Unit) {
        loadOrders()
        session.getDeliveryPersonId()?.let { driverId ->
            SignalRManager.connect(driverId) {
                scope.launch { loadOrders() }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { SignalRManager.disconnect() }
    }

    // -------------------------------
    // UI
    // -------------------------------
    Scaffold(
        containerColor = bgColor,
        topBar = {
            TopAppBar(title = { Text("Pending Deliveries", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) })
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primary),
                onClick = { showCheckoutDialog = true }
            ) {
                Text("Checkout / End Shift", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            }
        }
    ) { padding ->

        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No pending deliveries", color = textSecondary)
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(orders) { order ->
                PendingOrderCard(
                    order = order,
                    primary = primary,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary,
                    onNavigate = {
                        order.address?.let { MapUtils.openGoogleMaps(context, it) }
                    },
                    onViewDetails = { onViewDetails(order) }
                )
            }
        }
    }

    // -------------------------------
    // Checkout confirmation dialog
    // -------------------------------
    if (showCheckoutDialog) {
        AlertDialog(
            onDismissRequest = { showCheckoutDialog = false },
            title = { Text("End Shift") },
            text = { Text("Are you sure you want to checkout and end your shift?") },
            confirmButton = {
                TextButton(onClick = {
                    showCheckoutDialog = false
                    onCheckout()
                }) {
                    Text("Yes, Checkout")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCheckoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun PendingOrderCard(
    order: PendingOrder,
    primary: Color,
    textPrimary: Color,
    textSecondary: Color,
    onNavigate: () -> Unit,
    onViewDetails: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Order Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Order #${order.orderCode}",
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = textPrimary,
                    style = MaterialTheme.typography.titleMedium
                )

                Surface(
                    color = Color(0xFFFFF3CD),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "Pending",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = primary,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Customer Info
            Text("Customer", color = textSecondary, style = MaterialTheme.typography.labelMedium)
            Text(order.customerName ?: "N/A", fontWeight = androidx.compose.ui.text.font.FontWeight.Medium, color = textPrimary)

            Spacer(modifier = Modifier.height(8.dp))

            // Address
            Text("Delivery Address", color = textSecondary, style = MaterialTheme.typography.labelMedium)
            Text(order.address ?: "N/A", color = textPrimary, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color(0xFFE9ECEF))
            Spacer(modifier = Modifier.height(12.dp))

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onViewDetails
                ) {
                    Text("Details")
                }

                Button(
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = primary, contentColor = Color.Black),
                    onClick = onNavigate
                ) {
                    Text("Navigate", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                }
            }
        }
    }
}