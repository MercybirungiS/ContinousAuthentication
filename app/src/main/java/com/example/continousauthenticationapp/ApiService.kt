package com.example.continousauthenticationapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("your-touch-endpoint")
    fun sendTouchData(@Body data: Map<String, Any>): Call<Void>

    @POST("your-keystroke-endpoint")
    fun sendKeystrokeData(@Body data: Map<String, Any>): Call<Void>

    @POST("your-power-endpoint")
    fun sendPowerData(@Body data: Map<String, Any>): Call<Void>
}
