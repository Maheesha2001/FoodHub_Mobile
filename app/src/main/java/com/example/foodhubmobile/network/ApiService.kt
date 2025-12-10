package com.example.foodhubmobile.network

import com.example.foodhubmobile.models.Order
import com.example.foodhubmobile.models.LoginRequest
import com.example.foodhubmobile.models.LoginResponse
import retrofit2.Response
import retrofit2.http.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
interface ApiService {

    @POST("api/delivery/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("api/delivery/pending")
    suspend fun getPendingOrders(): Response<List<Order>>

    @GET("api/orders/{id}")
    suspend fun getOrderDetails(@Path("id") id: String): Response<Order>

    @PUT("api/orders/complete/{id}")
    suspend fun markDelivered(
        @Path("id") id: String,
        @Body paymentReceived: Boolean
    ): Response<Map<String, String>>
}