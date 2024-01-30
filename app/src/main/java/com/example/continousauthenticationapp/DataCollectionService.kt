package com.example.continousauthenticationapp


import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.view.KeyEvent
import android.view.MotionEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataCollectionService : Service() {

    private val touchCollector: TouchCollector by lazy { TouchCollector(applicationContext) }
    private val keystrokeCollector: KeystrokeCollector by lazy { KeystrokeCollector() }
    private val powerCollector: PowerCollector by lazy { PowerCollector(applicationContext) }
    private val apiService: ApiService by lazy { ApiUtils.apiService }

    private val handler = Handler()
    private lateinit var touchDataCollectionRunnable: Runnable
    private lateinit var keystrokeDataCollectionRunnable: Runnable
    private lateinit var powerDataCollectionRunnable: Runnable

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startDataCollection()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startDataCollection() {
        touchDataCollectionRunnable = object : Runnable {
            override fun run() {
                val touchData = touchCollector.collectTouchData(getDummyMotionEvent())
                sendDataToAPI(touchData, "sendTouchData")
                handler.postDelayed(this, 10000) // Collect touch data every 10 seconds
            }
        }

        keystrokeDataCollectionRunnable = object : Runnable {
            override fun run() {
                val keystrokeData = keystrokeCollector.collectKeystrokeData(getDummyKeyEvent())
                sendDataToAPI(keystrokeData, "sendKeystrokeData")
                handler.postDelayed(this, 10000) // Collect keystroke data every 10 seconds
            }
        }

        powerDataCollectionRunnable = object : Runnable {
            override fun run() {
                val powerData = powerCollector.collectPowerData()
                sendDataToAPI(powerData, "sendPowerData")
                handler.postDelayed(this, 10000) // Collect power data every 10 seconds
            }
        }

        // Start collecting data immediately
        handler.post(touchDataCollectionRunnable)
        handler.post(keystrokeDataCollectionRunnable)
        handler.post(powerDataCollectionRunnable)
    }

    private fun sendDataToAPI(data: Map<String, Any>, endpoint: String) {
        when (endpoint) {
            "sendTouchData" -> {
                apiService.sendTouchData(data).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        // Handle successful API call
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        // Handle failed API call
                    }
                })
            }
            "sendKeystrokeData" -> {
                apiService.sendKeystrokeData(data).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        // Handle successful API call
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        // Handle failed API call
                    }
                })
            }
            "sendPowerData" -> {
                apiService.sendPowerData(data).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        // Handle successful API call
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        // Handle failed API call
                    }
                })
            }
            // Add more cases for other endpoints if needed
        }
    }

    // Dummy MotionEvent for testing, replace this with actual data
    private fun getDummyMotionEvent(): MotionEvent {
        // Your logic to create a dummy MotionEvent
        return MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 0f, 0f, 0)
    }

    // Dummy KeyEvent for testing, replace this with actual data
    private fun getDummyKeyEvent(): KeyEvent {
        // Your logic to create a dummy KeyEvent
        return KeyEvent(0, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop data collection when the service is destroyed
        handler.removeCallbacks(touchDataCollectionRunnable)
        handler.removeCallbacks(keystrokeDataCollectionRunnable)
        handler.removeCallbacks(powerDataCollectionRunnable)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

