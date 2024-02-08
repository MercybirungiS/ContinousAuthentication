package com.example.biometricapp.model

data class KeyboardMetrics(
    val device_id: String,
    val android_version: String,
    val flight_time: Long,
    val key_hold_time: Long,
    val finger_pressure: Float,
    val finger_area: Float

)
