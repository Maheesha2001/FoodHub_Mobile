package com.example.foodhubmobile.network

import com.example.foodhubmobile.models.Order
import com.example.foodhubmobile.models.LoginRequest
import com.example.foodhubmobile.models.LoginResponse
import com.example.foodhubmobile.models.PendingOrder
import retrofit2.Response
import retrofit2.http.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
interface ApiService {

    @POST("api/DeliveryApi/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("api/DeliveryApi/pending")
    suspend fun getPendingOrders(): Response<List<PendingOrder>>

    @GET("api/orders/{id}")
    suspend fun getOrderDetails(@Path("id") id: String): Response<Order>

    @PUT("api/orders/complete/{id}")
    suspend fun markDelivered(
        @Path("id") id: String,
        @Body paymentReceived: Boolean
    ): Response<Map<String, String>>

    @POST("api/DeliveryApi/attendance")
    fun markAttendance(
        @Header("deliveryPersonId") deliveryPersonId: String,
        @Body date: String
    ): Call<Unit>

    @GET("api/DeliveryApi/order/{code}")
    suspend fun getOrderByCode(
        @Path("code") code: String
    ): Response<Order>

    @POST("api/DeliveryApi/mark-delivered/{code}")
    suspend fun markDelivered(
        @Path("code") code: String
    ): Response<Unit>


}