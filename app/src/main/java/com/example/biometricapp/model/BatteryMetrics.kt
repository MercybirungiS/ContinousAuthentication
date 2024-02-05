package com.example.biometricapp.model

data class BatteryMetrics(
    val deviceId: String,
    val androidVersion: String,
    val voltage: Float,
    val current: Float
)
