package com.example.googlemapsproject.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.googlemapsproject.util.Constants.ACTION_SERVICE_START
import com.example.googlemapsproject.util.Constants.ACTION_SERVICE_STOP
import com.example.googlemapsproject.util.Constants.NOTIFICATION_CHANNEL_ID
import com.example.googlemapsproject.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.googlemapsproject.util.Constants.NOTIFICATION_ID
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class TrackerService : LifecycleService() {

    @Inject
    lateinit var notification: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager


    companion object {
        val started = MutableLiveData<Boolean>()
    }

    private fun setInitialValues() {
        started.postValue(false)
    }

    override fun onCreate() {
        setInitialValues()
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (it.action) {
                ACTION_SERVICE_START -> {
                    started.postValue(true)
                    startForegroundService()
                }

                ACTION_SERVICE_STOP -> {
                    started.postValue(false)
                }
            }
        }

        return START_STICKY
    }


    private fun startForegroundService() {
        createNotificationChannel()
        val notification = notification.build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Running service to find your location"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

}







