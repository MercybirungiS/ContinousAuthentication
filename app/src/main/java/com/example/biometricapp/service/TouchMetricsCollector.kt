package com.example.biometricapp.service

import android.view.MotionEvent

class TouchMetricsCollector {

    fun calculateFingerPressure(event: MotionEvent): Float {
        return event.pressure
    }

    fun calculateHoldTime(event: MotionEvent): Float {
        return (event.eventTime - event.downTime).toFloat()
    }

    fun calculateFingerBlockedArea(event: MotionEvent): Float {
        return event.size
    }

    fun calculateFingerOrientation(event: MotionEvent): String {
        val azimuth = event.getOrientation(0)
        return Math.toDegrees(azimuth.toDouble()).toString()
    }
}








