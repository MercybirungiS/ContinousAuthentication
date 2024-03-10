package com.example.biometricapp.service

import android.view.MotionEvent
import java.lang.Math.PI
import java.lang.Math.atan2
import java.lang.Math.toDegrees
import kotlin.random.Random

class TouchMetricsCollector {



    private var baselineArea: Float = 0f
    private var pressureScore: Float = 0f
    private var lastX: Float = 0f
    private var lastY: Float = 0f
    private val AREA_THRESHOLD = 0.0f // Adjust this value
    private val MOVEMENT_THRESHOLD = 0f // Adjust this value

//    fun calculateFingerPressure(event: MotionEvent): Float {
//    return event.getPressure()
//    }


//    fun calculateFingerPressure(event: MotionEvent): Float {
//        val basePressure = Random.nextDouble(0.0, 1.0) // Include 0, but not 1.0
//        val variation = Random.nextDouble(0.0, 0.1)   // Smaller positive-only variation
//
//        var simulatedPressure = basePressure + variation
//
//        // Ensure the pressure stays within the 0 to 1.0 range
//        simulatedPressure = simulatedPressure.coerceAtLeast(0.0) // Ensure no negativity
//        simulatedPressure = simulatedPressure.coerceAtMost(1.0)
//
//        return simulatedPressure.toFloat()
//    }



    private var previousSimulatedPressure = 0.5f // Start with a middle-range pressure

    fun calculateFingerPressure(event: MotionEvent): Float {
        // ... (Your existing logic for base pressure and variation) ...

        // Calculate Pressure Change
        val pressureChangeMagnitude = Random.nextDouble(0.05, 1.0) // Adjust range as needed
        val pressureChangeDirection = if (Random.nextBoolean()) 1 else -1
        val pressureChange = pressureChangeMagnitude * pressureChangeDirection

        // Apply Change and Clamp
        var simulatedPressure = previousSimulatedPressure + pressureChange
        simulatedPressure = simulatedPressure.coerceAtLeast(0.0)
        simulatedPressure = simulatedPressure.coerceAtMost(1.0)

        previousSimulatedPressure = simulatedPressure.toFloat() // Store for the next calculation

        return simulatedPressure.toFloat()
    }



    //  ... (Constants and Variables from the previous complete algorithm) ...




    fun calculateHoldTime(event: MotionEvent): Float {
        return event.touchMajor/0.4f

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