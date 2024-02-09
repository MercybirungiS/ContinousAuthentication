package com.example.biometricapp.service


class KeyMetricsCollector {

    private var lastKeyPressTimestamp: Long = 0
    private var totalFlightTime: Long = 0
    private var totalKeyholdTime: Long = 0
    private var totalFingerPressure: Float = 0.0f
    private var totalFingerArea: Float = 0.0f
    private var keyCount: Int = 0

    fun onKeyPress() {
        val currentTimestamp = System.currentTimeMillis()

        if (lastKeyPressTimestamp != 0L) {
            val flightTime = currentTimestamp - lastKeyPressTimestamp
            totalFlightTime += flightTime
        }

        lastKeyPressTimestamp = currentTimestamp
        keyCount++
    }

    fun onKeyRelease(fingerPressure: Float, fingerArea: Float) {
        val keyholdTime = System.currentTimeMillis() - lastKeyPressTimestamp

        totalKeyholdTime += keyholdTime
        totalFingerPressure += fingerPressure
        totalFingerArea += fingerArea
    }

    fun calculateFlightTime(): Long {
        return if (keyCount > 0) totalFlightTime / keyCount else 0
    }

    fun calculateKeyholdTime(): Long {
        return if (keyCount > 0) totalKeyholdTime / keyCount else 0
    }

    fun calculateAverageFingerPressure(): Float {
        return if (keyCount > 0) totalFingerPressure / keyCount else 0.0f
    }

    fun calculateAverageFingerArea(): Float {
        return if (keyCount > 0) totalFingerArea / keyCount else 0.0f
    }
}


