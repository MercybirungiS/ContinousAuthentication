package com.example.biometricapp.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.provider.Settings.*
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.biometricapp.R
import com.example.biometricapp.model.BatteryMetrics
import com.example.biometricapp.model.KeyboardMetrics
import com.example.biometricapp.model.TouchMetrics
import com.example.biometricapp.network.CustomHttpError
import com.example.biometricapp.network.RetrofitClient
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit


class ContinuousAuthService : Service() {

    private lateinit var executorService: ScheduledFuture<*>
    private val handler = Handler()
    private lateinit var keyMetricsCollector: KeyMetricsCollector
    private lateinit var touchMetricsCollector: TouchMetricsCollector
    private lateinit var batteryMetricsCollector: BatteryMetricsCollector
    private val CHANNEL_ID = "ContinuousAuthServiceChannel"
    private val API_KEY=  "ahtywFO4NeUGkopSTnHFd2GTAXepF2ah"

    private val NOTIFICATION_ID = 1
    private val POST_NOTIFICATIONS_PERMISSION_CODE = 456
    private lateinit var overlayView: View
    private lateinit var windowManager: WindowManager




    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onCreate() {
        super.onCreate()

        keyMetricsCollector = KeyMetricsCollector()
        touchMetricsCollector = TouchMetricsCollector()
        batteryMetricsCollector = BatteryMetricsCollector(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !canDrawOverlays(this)) {
            // Permission is not granted, request it
            val intent = Intent(ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } else {
            // Permission is granted, show the overlay view
            showOverlayView()
        }


        // Start collecting metrics every 5 seconds
        executorService = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
            {


                collectMetrics()
            },
            0,
            5,
            TimeUnit.SECONDS
        )

        createNotificationChannel()
        // showNotification()
        handler.postDelayed({
            startForegroundService(this)
        }, 1000)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("ContinuousAuthService is running")
            .setContentText("Collecting metrics in the background")
            .setSmallIcon(R.drawable.ic_notification_icon)
            .build()
        startForeground(NOTIFICATION_ID, notification)



    }

//    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && canDrawOverlays(this)) {
//                // Permission granted, show the overlay view
//                showOverlayView()
//            } else {
//                // Permission denied, handle accordingly (e.g., show a message to the user)
//                Log.e("ContinuousAuthService", "Overlay permission denied")
//            }
//        }
//    }

    private fun showOverlayView() {
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.BOTTOM

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_view, null)
        windowManager.addView(overlayView, params)

        overlayView.setOnTouchListener { _, event ->
            processTouchEvent(event)
            collectKeyboardMetrics(event)
            true
        }
    }

    private fun collectMetrics() {
        // Collect keyboard metrics
//        collectKeyboardMetrics()

//         Collect touch metrics

//        collectTouchMetrics(  )

        // Collect battery metrics
        collectBatteryMetrics()

    }

    private fun collectKeyboardMetrics(event: MotionEvent) {
        val deviceId = fetchDeviceId(this)
        val androidVersion = Build.VERSION.RELEASE
        val flightTime = keyMetricsCollector.calculateFlightTime(event)
        val keyholdTime = keyMetricsCollector.calculateKeyholdTime(event)
        val fingerPressure = keyMetricsCollector.calculateAverageFingerPressure()
        val fingerArea = keyMetricsCollector.calculateAverageFingerArea()

        // Send keyboard metrics to backend API
        if (isNetworkAvailable() && isInternetAvailable(this)) {
            sendDataToKeyboardMetricsEndpoint(deviceId, androidVersion, flightTime, keyholdTime, fingerPressure, fingerArea)
            Log.d("AuthServiceKeyboard", " $deviceId,  $androidVersion ,$flightTime,$keyholdTime,$fingerPressure,$fingerArea"  )

        } else {
            Log.e("ContinuousAuthService", "Network or internet not available")
        }
    }

    private fun processTouchEvent(event: MotionEvent) {

        val device_id = fetchDeviceId(this)
        val android_version = Build.VERSION.RELEASE
        val finger_pressure = touchMetricsCollector.calculateFingerPressure(event)
        val hold_time = touchMetricsCollector.calculateHoldTime(event)
        val finger_blocked_area = touchMetricsCollector.calculateFingerBlockedArea(event)
        val finger_orientation = touchMetricsCollector.calculateFingerOrientation(event)

        // Send touch metrics to backend API
        if (isNetworkAvailable() && isInternetAvailable(this)) {
            sendDataToTouchMetrics(device_id, android_version, finger_pressure, finger_blocked_area, hold_time, finger_orientation)

            Log.d("AuthServiceTouch","$device_id, $android_version, $finger_pressure, $finger_blocked_area, $hold_time, $finger_orientation")
        } else {
            Log.e("ContinuousAuthService", "Network or internet not available")
        }
    }





    private fun collectBatteryMetrics() {
        val deviceId = fetchDeviceId(this)
        val androidVersion = Build.VERSION.RELEASE
        val voltage = batteryMetricsCollector.calculateVoltage()
        val current = batteryMetricsCollector.calculateCurrent(this)

        // Send battery metrics to backend API
        if (isNetworkAvailable() && isInternetAvailable(this )) {
            sendDataToBatteryMetricsEndpoint(deviceId, androidVersion, voltage, current)
            Log.d("AuthServiceBattery", " $deviceId,  $androidVersion ,$voltage,$current"  )

        } else {
            Log.e("ContinuousAuthService", "Network or internet not available")
        }
    }

    @SuppressLint("HardwareIds")
    private fun fetchDeviceId(context: Context): String {
        return Secure.getString(context.contentResolver, Secure.ANDROID_ID) ?: ""
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager?.activeNetwork
            val capabilities = connectivityManager?.getNetworkCapabilities(network)
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        } else {
            val activeNetworkInfo = connectivityManager?.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }


    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager?.activeNetwork
            val capabilities = connectivityManager?.getNetworkCapabilities(network)
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        } else {
            val activeNetworkInfo = connectivityManager?.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

    private fun sendDataToKeyboardMetricsEndpoint(deviceId: String, androidVersion: String, flightTime: Float, keyholdTime: Float, fingerPressure: Float, fingerArea: Float) {
        // Implement API call to your backend for keyboard metrics using Retrofit

        val keyboardMetrics = KeyboardMetrics(deviceId, androidVersion, flightTime, keyholdTime, fingerPressure, fingerArea)

        RetrofitClient.getInstance().sendKeyboardMetrics(API_KEY, keyboardMetrics).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.i("ContinuousAuthService", "Keyboard metrics sent successfully :${response.isSuccessful}")
                } else {
                    Log.e("ContinuousAuthService", "Failed to send keyboard metrics. Code: ${response}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ContinuousAuthService", "Failed to send keyboard metrics. Error: ${t.message}")
            }
        })
    }


    private fun sendDataToTouchMetrics(deviceId: String, androidVersion: String, finger_pressure: Float, finger_blocked_area: Float, hold_time: Float, finger_orientation: String) {
        // Implement API call to your backend for keyboard metrics using Retrofit

        val touchMetrics = TouchMetrics(deviceId, androidVersion, finger_pressure, finger_blocked_area, hold_time, finger_orientation)

        RetrofitClient.getInstance().sendTouchMetrics(API_KEY, touchMetrics).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.i("ContinuousAuthService", "Touch metrics sent successfully :${response.isSuccessful}")
                } else {
                    Log.e("ContinuousAuthService", "Failed to send touch metrics. Code: ${
                        response.errorBody()?.string().toString()}")

                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                if (t is HttpException) {
                    val body: ResponseBody? = (t as HttpException).response()?.errorBody()
                    val gson = Gson()
                    val adapter: TypeAdapter<CustomHttpError> = gson.getAdapter(CustomHttpError::class.java)
                    try {
                        val error: CustomHttpError = adapter.fromJson(body?.string() )
                        Log.i(TAG, "TouchmetricsresposeError:" + error.code + " and message = "+error.message+"")
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

            }
        })
    }



    private fun sendDataToBatteryMetricsEndpoint(deviceId: String, androidVersion: String, voltage: Float, current: Float) {
        //  API call to your backend for battery metrics using Retrofit

        val batteryMetrics = BatteryMetrics(deviceId, androidVersion, voltage, current)


        RetrofitClient.getInstance().sendBatteryMetrics(API_KEY, batteryMetrics).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.i("ContinuousAuthService", "Battery metrics sent successfully :${response}")

                } else {
                    try {

                    } catch (e: Exception) {
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ContinuousAuthService", "Failed to send battery metrics. Error: ${t.message}")
            }
        })
    }


    private fun showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "ContinuousAuthService Channel"
            val descriptionText = "Channel for ContinuousAuthService"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("ContinuousAuthService is running")
            .setContentText("Collecting metrics in the background")
            .setSmallIcon(R.drawable.ic_notification_icon)
            .build()

        val notificationManager = NotificationManagerCompat.from(this)

        // Check if the app has the POST_NOTIFICATIONS permission
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If the permission is not granted, request it
            ActivityCompat.requestPermissions(
                this as Activity,  // Cast to Activity
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                POST_NOTIFICATIONS_PERMISSION_CODE
            )
        } else {
            // Permission is already granted, show the notification
//            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }




    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "ContinuousAuthService Channel"
            val descriptionText = "Channel for ContinuousAuthService"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        executorService.cancel(true)
    }

    companion object {
        fun startService(context: Context) {
            val serviceIntent = Intent(context, ContinuousAuthService::class.java)
            context.startService(serviceIntent)
        }


        fun startForegroundService(context: Context) {
            val serviceIntent = Intent(context, ContinuousAuthService::class.java)
            ContextCompat.startForegroundService(context, serviceIntent)
        }
        fun stopService(context: Context) {
            val serviceIntent = Intent(context, ContinuousAuthService::class.java)
            context.stopService(serviceIntent)
        }
        @SuppressLint("ServiceCast")
        fun isServiceRunning(context: Context, java: Class<ContinuousAuthService>): Boolean {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            if (manager != null) {
                for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
                    if (ContinuousAuthService::class.java.name == service.service.className) {
                        return true
                    }
                }
            }
            return false
        }
    }
}