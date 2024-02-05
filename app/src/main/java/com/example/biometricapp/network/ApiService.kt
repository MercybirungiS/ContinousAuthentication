package com.example.biometricapp.network

// network/ApiService.kt
import com.example.biometricapp.model.BatteryMetrics
import com.example.biometricapp.model.KeyboardMetrics
import com.example.biometricapp.model.TouchMetrics
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("virtual-keyboard-metrics/create")
    fun sendKeyboardMetrics(
        @Header("Authorization") apiKey: String,
        @Body keyboardMetrics: KeyboardMetrics
    ): Call<Void>


    @POST("touch-dynamics/create")
    fun sendTouchMetrics(
        @Header("Authorization") apiKey: String,
        @Body touchMetrics: TouchMetrics
    ): Call<Void>

    @POST("battery-metrics/create")
    fun sendBatteryMetrics(
        @Header("Authorization") apiKey: String,
        @Body batteryMetrics: BatteryMetrics
    ): Call<Void>
}
