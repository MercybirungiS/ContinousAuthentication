package com.example.biometricapp.model

data class TouchMetrics(
    val device_id: String,
    val android_version: String,
    val finger_pressure: Float,
    val finger_blocked_area: Float,
    val hold_time: Float,
    val finger_orientation: String,
)
