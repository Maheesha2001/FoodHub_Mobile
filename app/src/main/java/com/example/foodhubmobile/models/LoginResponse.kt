package com.example.foodhubmobile.models

data class LoginResponse(
    val success: Boolean,
    val name: String?,
    val message: String? = null,
    val deliveryPersonId: String?
)
