package com.example.foodhubmobile

import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.foodhubmobile.models.CheckoutRequest
import com.example.foodhubmobile.models.PendingOrder
import com.example.foodhubmobile.ui.screens.OrderDetailsScreen
import com.example.foodhubmobile.ui.screens.PendingOrdersScreen
import com.example.foodhubmobile.utils.SessionManager
import com.example.foodhubmobile.network.ApiClient
import kotlinx.coroutines.launch

@Composable
fun FoodHubNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val api = ApiClient.retrofit.create(com.example.foodhubmobile.network.ApiService::class.java)
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = "pending_orders"
    ) {

        // -------------------------------
        // PENDING ORDERS SCREEN
        // -------------------------------
        composable("pending_orders") {
            PendingOrdersScreen(
                session = sessionManager,
                navController = navController,
                onViewDetails = { order: PendingOrder ->
                    // Navigate to order details screen
                    navController.navigate("order_details/${order.orderCode}")
                },
                onCheckout = {
                    // Checkout API call
                    val driverId = sessionManager.getDeliveryPersonId()
                    if (!driverId.isNullOrEmpty()) {
                        scope.launch {
                            try {
                                val response = api.checkout(CheckoutRequest(driverId))
                                if (response.isSuccessful) {
                                    Toast.makeText(
                                        context,
                                        "Checked out successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    // Launch AttendanceActivity
                                    val intent = Intent(context, AttendanceActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    context.startActivity(intent)
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Checkout failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    "Error: ${e.localizedMessage}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Delivery person ID not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }

        // -------------------------------
        // ORDER DETAILS SCREEN
        // -------------------------------
        composable(
            route = "order_details/{orderCode}",
            arguments = listOf(navArgument("orderCode") { type = NavType.StringType })
        ) { entry ->
            val orderCode = entry.arguments?.getString("orderCode") ?: return@composable
            OrderDetailsScreen(
                orderCode = orderCode,
                onComplete = {
                    navController.popBackStack()
                }
            )
        }
    }
}
