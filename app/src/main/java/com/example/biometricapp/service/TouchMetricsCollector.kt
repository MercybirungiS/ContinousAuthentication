package com.example.biometricapp.service

import android.view.MotionEvent

class TouchMetricsCollector {

    private var lastEvent: MotionEvent? = null

    fun onEvent(event: MotionEvent) {
        lastEvent = event
    }

    fun calculateFingerPressure(): Float {
        lastEvent?.let {
            // Get pressure from the stored MotionEvent
            return it.pressure
        }
        return 0f // or handle the case where no event is available
    }

    fun calculateHoldTime(): Long {
        lastEvent?.let {
            // Get the time the event occurred
            return it.eventTime - it.downTime
        }
        return 0L // or handle the case where no event is available
    }

    fun calculateFingerBlockedArea(): Float {
        lastEvent?.let {
            return it.size
        }
        return 0f
    }

    fun calculateFingerOrientation(): Float {
        lastEvent?.let {
            val azimuth = it.getOrientation(0)
            // Convert azimuth to degrees
            return Math.toDegrees(azimuth.toDouble()).toFloat()
        }
        return 0f
    }
}
