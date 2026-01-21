package com.example.foodhubmobile.models

data class PendingOrder(
    val orderCode: String,
    val customerName: String,
    val address: String,
    val dilveryName: String
)
