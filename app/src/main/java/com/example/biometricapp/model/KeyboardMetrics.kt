package com.example.biometricapp.model

data class KeyboardMetrics(
    val deviceId: String,
    val androidVersion: String,
    val flightTime: Long,
    val keyholdTime: Long,
    val fingerPressure: Float,
    val fingerArea: Float

)
