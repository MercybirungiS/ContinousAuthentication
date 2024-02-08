package com.example.biometricapp.model

data class TouchMetrics(
    val device_d: String,
    val android_version: String,
    val finger_pressure: Float,
    val hold_time: Long,
    val finger_blocked_area: Float,
    val finger_orientation: Float
)
