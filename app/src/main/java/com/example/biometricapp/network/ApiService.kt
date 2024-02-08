package com.example.biometricapp.network

// network/ApiService.kt
import com.example.biometricapp.model.BatteryMetrics
import com.example.biometricapp.model.KeyboardMetrics
import com.example.biometricapp.model.TouchMetrics
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Content-Type:application/json")
    @POST("/api/virtual-keyboard-metrics/create")
    fun sendKeyboardMetrics(
        @Header("X-API-Key") apiKey: String,
        @Body keyboardMetrics: KeyboardMetrics
    ): Call<Void>


    @Headers("Content-Type:application/json")
    @POST("/api/touch-dynamics/create")
    fun sendTouchMetrics(
        @Header("X-API-Key") apiKey: String,
        @Body touchMetrics: TouchMetrics
    ): Call<Void>

    @Headers("Content-Type:application/json")
    @POST("/api/battery-metrics/create")
    fun sendBatteryMetrics(
        @Header("X-API-Key") apiKey: String,
        @Body batteryMetrics: BatteryMetrics
    ): Call<Void>
}
