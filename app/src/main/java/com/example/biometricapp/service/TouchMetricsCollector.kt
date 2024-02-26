package com.example.biometricapp.service

import android.view.MotionEvent
import java.lang.Math.PI
import java.lang.Math.atan2
import java.lang.Math.toDegrees

class TouchMetricsCollector {



    private var baselineArea: Float = 0f
    private var pressureScore: Float = 0f
    private var lastX: Float = 0f
    private var lastY: Float = 0f
    private val AREA_THRESHOLD = 0.0f // Adjust this value
    private val MOVEMENT_THRESHOLD = 0f // Adjust this value

    fun calculateFingerPressure(event: MotionEvent): Float {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                baselineArea = event.size
                pressureScore = 0f // Reset score
                lastX = event.x
                lastY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val areaDifference = event.size - baselineArea
                if (areaDifference > AREA_THRESHOLD) {
                    pressureScore += 1f
                }

                val deltaX = event.x - lastX
                val deltaY = event.y - lastY
                val movementMagnitude = deltaX * deltaX + deltaY * deltaY

                if (movementMagnitude > MOVEMENT_THRESHOLD) {
                    pressureScore += 0.5f // Adjust weight as needed
                }

                lastX = event.x
                lastY = event.y
            }
        }
        return pressureScore
    }

    fun calculateHoldTime(event: MotionEvent): Float {
        val pointerCount = event.pointerCount
        var maxHoldTime = 0L

        for (i in 0 until pointerCount) {
            val downTime = event.getDownTime()
            val eventTime = event.eventTime
            val holdTime = eventTime - downTime

            if (holdTime > maxHoldTime) {
                maxHoldTime = holdTime
            }
        }

        return maxHoldTime.toFloat()
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