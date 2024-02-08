package com.example.biometricapp

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.os.Bundle
import android.Manifest
import android.content.Intent
import android.util.Log
import android.view.MotionEvent
import com.example.biometricapp.service.ContinuousAuthService
import com.example.biometricapp.service.TouchMetricsCollector

class MainActivity : AppCompatActivity() {

    private val INTERNET_PERMISSION_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e("ContinuousAuthService", "working")


        // Check for internet permission and start the service accordingly
        checkInternetPermission()
    }

    private fun startContinuousAuthService() {

        ContinuousAuthService.startService(this)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Pass the touch event to the TouchMetricsCollector
        val touchMetricsCollector =TouchMetricsCollector()
        touchMetricsCollector.onEvent(event)
        return super.onTouchEvent(event)
    }


    private fun checkInternetPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If the permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.INTERNET),
                INTERNET_PERMISSION_CODE
            )
        } else {

            if (ContinuousAuthService.isServiceRunning(this, ContinuousAuthService::class.java)) {

                Log.i("MainActivity", "ContinuousAuthService is already running")

            } else {
                // Service is not running, start it
                startContinuousAuthService()
            }
        }
    }

    // Handle the result of the permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == INTERNET_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (!ContinuousAuthService.isServiceRunning(this, ContinuousAuthService::class.java)) {
                    // Service is not running, start it
                    startContinuousAuthService()
                    Log.i("MainActivity", "ContinuousAuthService is already running")

                }
            } else {
                Log.e("MainActivity", "Internet permission denied")
            }
        }
    }

    override fun onDestroy() {
        // Stop the service when the activity is destroyed
        ContinuousAuthService.stopService(this)
        super.onDestroy()
    }
}
