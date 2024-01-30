package com.example.continousauthenticationapp

import android.view.KeyEvent

class KeystrokeCollector {
    private var lastKeyPressTime: Long = 0

    fun collectKeystrokeData(event: KeyEvent): Map<String, Any> {
        val flightTime = calculateFlightTime(event)
        val keyHoldTime = calculateKeyHoldTime(event)
        val fingerPressure = calculateFingerPressure(event)
        val fingerArea = calculateFingerArea()

        return mapOf(
            "flightTime" to flightTime,
            "keyHoldTime" to keyHoldTime,
            "fingerPressure" to fingerPressure,
            "fingerArea" to fingerArea
        )
    }

    private fun calculateFlightTime(event: KeyEvent): Long {
        return System.currentTimeMillis() - lastKeyPressTime
    }

    private fun calculateKeyHoldTime(event: KeyEvent): Long {
        if (event.action == KeyEvent.ACTION_DOWN) {
            lastKeyPressTime = System.currentTimeMillis()
        }
        return System.currentTimeMillis() - lastKeyPressTime
    }

    private fun calculateFingerPressure(event: KeyEvent): Float {
        // Placeholder logic: Assuming constant pressure for all key events
        return 0.5f
    }

    private fun calculateFingerArea(): Float {
        // Placeholder logic: Assuming constant area for all key events
        return 30.0f
    }
}
