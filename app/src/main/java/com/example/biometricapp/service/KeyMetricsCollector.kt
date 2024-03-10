package com.example.biometricapp.service

import android.view.MotionEvent


class KeyMetricsCollector {







    fun calculateFlightTime(event: MotionEvent): Float {
        return event.touchMajor/0.2f
    }

    fun calculateKeyholdTime(event: MotionEvent): Float {
        return event.touchMajor/0.4f

    }

    fun calculateAverageFingerPressure(): Float {
        return  0.0f
    }

    fun calculateAverageFingerArea(): Float {
        return 0.0f
    }
}


