package com.example.continousauthenticationapp

import android.content.Context
import android.view.MotionEvent
import kotlin.math.atan2
import kotlin.math.hypot

class TouchCollector(private val context: Context) {
    private var touchStartTime: Long = 0

    fun collectTouchData(event: MotionEvent): Map<String, Any> {
        val fingerPressure = event.pressure
        val blockedArea = calculateBlockedArea(event)
        val holdTime = calculateHoldTime(event)
        val fingerOrientation = calculateFingerOrientation(event)

        return mapOf(
            "fingerPressure" to fingerPressure,
            "blockedArea" to blockedArea,
            "holdTime" to holdTime,
            "fingerOrientation" to fingerOrientation
        )
    }

    private fun calculateBlockedArea(event: MotionEvent): Float {
        val touchArea = event.size * context.resources.displayMetrics.density
        return touchArea / (event.xPrecision * event.yPrecision)
    }

    private fun calculateHoldTime(event: MotionEvent): Long {
        if (event.action == MotionEvent.ACTION_DOWN) {
            touchStartTime = System.currentTimeMillis()
        }
        return System.currentTimeMillis() - touchStartTime
    }

    private fun calculateFingerOrientation(event: MotionEvent): Float {
        val x = event.x - event.xPrecision / 2
        val y = event.y - event.yPrecision / 2
        return Math.toDegrees(atan2(y.toDouble(), x.toDouble())).toFloat()
    }
}
