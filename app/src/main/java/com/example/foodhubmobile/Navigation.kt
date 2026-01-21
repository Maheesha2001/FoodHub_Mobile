package com.example.foodhubmobile

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.foodhubmobile.models.Order
import com.example.foodhubmobile.ui.screens.OrderDetailsScreen
import com.example.foodhubmobile.ui.screens.PendingOrdersScreen
import com.example.foodhubmobile.utils.SessionManager
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.net.URLDecoder
import androidx.compose.ui.platform.LocalContext

@Composable
fun FoodHubNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val session = SessionManager(context)

    NavHost(
        navController = navController,
        startDestination = "pending_orders"
    ) {

//        composable("pending_orders") {
//            PendingOrdersScreen { order ->
//                navController.navigate("order_details/${order.code}")
//            }
//        }
        composable("pending_orders") {
            PendingOrdersScreen(
                session = session, // pass session here
                onSelectOrder = { order ->
                    navController.navigate("order_details/${order.orderCode}")
                }
            )
        }

        composable(
            route = "order_details/{orderCode}",
            arguments = listOf(
                navArgument("orderCode") { type = NavType.StringType }
            )
        ) { entry ->
            val orderCode = entry.arguments?.getString("orderCode")!!

            OrderDetailsScreen(
                orderCode = orderCode,
                onComplete = {
                    navController.popBackStack()
                }
            )
        }
    }
}




//
//@Composable
//fun FoodHubNavHost() {
//    val navController = rememberNavController()
//
//    NavHost(
//        navController = navController,
//        startDestination = "pending_orders"
//    ) {
//        composable("pending_orders") {
//            PendingOrdersScreen(onSelectOrder = { order ->
//                val json = URLEncoder.encode(Json.encodeToString(order), "UTF-8")
//                navController.navigate("order_details/$json")
//            })
//        }
//
//        composable(
//            route = "order_details/{orderJson}",
//            arguments = listOf(navArgument("orderJson") { type = NavType.StringType })
//        ) { entry ->
//            val encoded = entry.arguments?.getString("orderJson") ?: ""
//            val json = URLDecoder.decode(encoded, "UTF-8")
//            val order = Json.decodeFromString<Order>(json)
//
//            OrderDetailsScreen(order = order) {
//                navController.popBackStack()
//            }
//        }
//    }
//}



//package com.example.foodhubmobile
//
//import androidx.compose.runtime.Composable
//import androidx.navigation.NavType
//import androidx.navigation.compose.*
//import androidx.navigation.navArgument
//import com.example.foodhubmobile.models.Order
//import com.example.foodhubmobile.ui.screens.OrderDetailsScreen
//import com.example.foodhubmobile.ui.screens.PendingOrdersScreen
//import kotlinx.serialization.encodeToString
//import kotlinx.serialization.decodeFromString
//import kotlinx.serialization.json.Json
//import java.net.URLEncoder
//import java.net.URLDecoder
//
//
//@Composable
//fun FoodHubNavHost() {
//
//    val navController = rememberNavController()
//
//    NavHost(
//        navController = navController,
//        startDestination = "pending_orders"
//    ) {
//
//        // -------------------------------
//        // PENDING ORDERS SCREEN
//        // -------------------------------
//        composable("pending_orders") {
//            PendingOrdersScreen(
//                onSelectOrder = { order ->
//
//                    // Convert Order → JSON → URL safe
//                    val json = URLEncoder.encode(
//                        Json.encodeToString(order),
//                        "UTF-8"
//                    )
//
//                    navController.navigate("order_details/$json")
//                }
//            )
//        }
//
//        // -------------------------------
//        // ORDER DETAILS SCREEN
//        // -------------------------------
//        composable(
//            route = "order_details/{orderJson}",
//            arguments = listOf(navArgument("orderJson") {
//                type = NavType.StringType
//            })
//        ) { entry ->
//
//            // Decode JSON
//            val encoded = entry.arguments?.getString("orderJson") ?: ""
//            val json = URLDecoder.decode(encoded, "UTF-8")
//            val order = Json.decodeFromString<Order>(json)
//
//            OrderDetailsScreen(
//                order = order,
//                onComplete = { navController.popBackStack() }
//            )
//        }
//    }
//}
