package com.example.biometricapp.service

import android.view.MotionEvent
import java.lang.Math.PI
import java.lang.Math.atan2
import java.lang.Math.toDegrees
import kotlin.random.Random

class TouchMetricsCollector {







    fun calculateFingerPressure(event: MotionEvent): Float {


        return event.pressure
    }








    fun calculateHoldTime(event: MotionEvent): Float {
        val downTime = event.eventTime
        val upTime = event.downTime + event.eventTime

        if (event.actionMasked == MotionEvent.ACTION_UP || event.actionMasked == MotionEvent.ACTION_POINTER_UP) {
            return (upTime - downTime).toFloat() / 1000 // Convert to seconds
        }

        return (upTime - downTime).toFloat() / 1000 // Convert to seconds
    }




    fun calculateFingerBlockedArea(event: MotionEvent): Float {
        return event.size
    }



    fun calculateFingerOrientation(event: MotionEvent): String {
        // Assuming the goal is to compare with the immediately previous touch position
        val prevX = if (event.historySize > 0) event.getHistoricalX(0, 0) else event.x
        val prevY = if (event.historySize > 0) event.getHistoricalY(0, 0) else event.y

        val dx = event.x - prevX
        val dy = event.y - prevY

        return atan2(dy.toDouble(), dx.toDouble()).toDegrees().toString()
    }

    fun Double.toDegrees(): Double {
        return this * 180.0 / PI

    }






}