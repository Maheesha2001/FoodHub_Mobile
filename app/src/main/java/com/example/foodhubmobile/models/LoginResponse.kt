package com.example.foodhubmobile.models

data class LoginResponse(
    val success: Boolean,
    val id: Int?,
    val name: String?,
    val token: String?,
    val message: String? = null
)
