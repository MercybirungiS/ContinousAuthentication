package com.example.biometricapp.service

import android.view.MotionEvent
import android.view.View

class TouchMetricsCollector {

    fun calculateFingerPressure(event: MotionEvent): Float {
        // Get pressure from the MotionEvent
        return event.pressure
    }

    fun calculateHoldTime(event: MotionEvent): Long {
        // Get the time the event occurred
        return event.eventTime - event.downTime
    }

    fun calculateFingerBlockedArea(event: MotionEvent): Float {


        return event.size
    }

//    fun calculateFingerOrientation(event: MotionEvent, view: View): Float {
//        val viewWidth = view.width.toFloat()
//
//        val orientationRatio = event.x / viewWidth
//
//        // Return the calculated ratio
//        return orientationRatio
//    }

    fun calculateFingerOrientation(event: MotionEvent): Float {

        val azimuth = event.getOrientation(0)

        // Convert azimuth to degrees
        return Math.toDegrees(azimuth.toDouble()).toFloat()
    }



}
