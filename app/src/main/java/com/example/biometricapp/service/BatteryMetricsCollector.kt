package com.example.biometricapp.service

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build

class BatteryMetricsCollector(private val context: Context) {

    fun calculateVoltage(): Float {
        val batteryIntent = getBatteryIntent()
        val voltage = batteryIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)
        return voltage / 1000.0f // Convert mV to V
    }

//    @Suppress("DEPRECATION")
//    fun calculateCurrent(): Float {
//        val batteryIntent = getBatteryIntent()
//
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            val current = batteryIntent.getIntExtra(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW.toString(), -1)
//            current / 1000.0f // Convert µA to mA
//        } else {
//            // For Android versions below 21, you can choose an alternative approach
//            // or set it to a default value based on your requirements
//            -1.0f
//        }
//    }


    fun calculateCurrent(context: Context): Float {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val currentMicroAmperes = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)
        return currentMicroAmperes / 1000.0f // Convert microamperes to milliamperes
    }

    private fun getBatteryIntent(): Intent {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        return context.registerReceiver(null, filter)!!
    }
}


