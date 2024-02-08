package com.example.biometricapp.model

data class BatteryMetrics(
    val device_id: String,
    val android_version: String,
    val voltage: Float,
    val current: Float
)
