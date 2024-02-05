package com.example.biometricapp.model

data class TouchMetrics(
    val deviceId: String,
    val androidVersion: String,
    val fingerPressure: Float,
    val holdTime: Long,
    val fingerBlockedArea: Float,
    val fingerOrientation: Float
)
