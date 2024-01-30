package com.example.continousauthenticationapp

import android.content.Context
import android.os.BatteryManager

class PowerCollector(private val context: Context) {
    fun collectPowerData(): Map<String, Any> {
        val voltage = getBatteryVoltage()
        val current = getBatteryCurrent()

        return mapOf(
            "voltage" to voltage,
            "current" to current
        )
    }

    private fun getBatteryVoltage(): Float {
        // Use BatteryManager to get battery voltage
        val batteryManager =
            context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

        // Check if the device supports the property
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            val batteryProperties = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            return batteryProperties.toFloat()
        } else {
            // Handle devices running older versions of Android
            return 0.0f
        }
    }

    private fun getBatteryCurrent(): Float {
        // Use BatteryManager to get battery current
        val batteryManager =
            context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW) / 1000.0f
    }
}

