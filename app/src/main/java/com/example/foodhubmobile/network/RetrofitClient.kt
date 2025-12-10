package com.example.foodhubmobile.network

object RetrofitClient {
    //private const val BASE_URL = "https://yourserver.com/"
    private const val BASE_URL = "http://192.168.8.134:5187/"


    val instance: ApiService by lazy {
        retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}