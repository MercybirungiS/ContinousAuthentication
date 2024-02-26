package com.example.biometricapp.model

data class KeyboardMetrics(
    val device_id: String,
    val android_version: String,
    val flight_time: Float,
    val key_hold_time: Float,
    val finger_pressure: Float,
    val finger_area: Float

)
