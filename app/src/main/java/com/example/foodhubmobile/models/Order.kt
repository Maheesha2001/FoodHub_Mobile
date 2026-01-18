    package com.example.foodhubmobile.models
    @kotlinx.serialization.Serializable
    data class Order(
        val id: Int,
        val code: String,
        val totalAmount: Double,
        val status: String,
        val createdAt: String,
        val items: List<OrderItem>
    )