package com.example.biometricapp.service

import android.view.MotionEvent


class KeyMetricsCollector {






    fun calculateFlightTime(event: MotionEvent): Float {
        // Calculate the duration between the down and up event
        val downTime = event.downTime
        val upTime = event.eventTime
        return (upTime - downTime).toFloat() / 1000 // Convert to seconds
    }

    fun calculateKeyholdTime(event: MotionEvent): Float {
        val downTime = event.eventTime
        val upTime = event.downTime + event.eventTime

        if (event.actionMasked == MotionEvent.ACTION_UP || event.actionMasked == MotionEvent.ACTION_POINTER_UP) {
            return (upTime - downTime).toFloat() / 1000 // Convert to seconds
        }

        return (upTime - downTime).toFloat() / 1000 // Convert to seconds
    }


    fun calculateAverageFingerPressure(): Float {
        return  0.0f
    }

    fun calculateAverageFingerArea(): Float {
        return 0.0f
    }
}


