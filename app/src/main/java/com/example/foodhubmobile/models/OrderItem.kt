    package com.example.foodhubmobile.models
    @kotlinx.serialization.Serializable
    data class OrderItem(
        val productId: String,
        val productName: String,
        val quantity: Int,
        val unitPrice: Double,
        val totalPrice: Double

    )

