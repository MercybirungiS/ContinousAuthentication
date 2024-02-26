package com.example.biometricapp.service

import android.view.MotionEvent


class KeyMetricsCollector {







    fun calculateFlightTime(event: MotionEvent): Float {
        return event.touchMajor
    }

    fun calculateKeyholdTime(event: MotionEvent): Float {
        return (event.eventTime - event.downTime).toFloat()

    }

    fun calculateAverageFingerPressure(): Float {
        return  0.0f
    }

    fun calculateAverageFingerArea(): Float {
        return 0.0f
    }
}


