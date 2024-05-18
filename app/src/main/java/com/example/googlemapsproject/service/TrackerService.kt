package com.example.googlemapsproject.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.googlemapsproject.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.googlemapsproject.util.Constants.ACTION_STOP_SERVICE
import com.example.googlemapsproject.util.Constants.NOTIFICATION_CHANNEL_ID
import com.example.googlemapsproject.util.Constants.NOTIFICATION_CHANNEL_NAME
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class TrackerService : LifecycleService() {

    @Inject
    lateinit var notificationCompat: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager

    companion object {
        val startedFromMain = MutableLiveData<Boolean>()
    }

    private fun setInitialValues() {
        startedFromMain.postValue(false)
    }

    override fun onCreate() {
        setInitialValues()
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    startedFromMain.postValue(true)
                }

                ACTION_STOP_SERVICE -> {
                    startedFromMain.postValue(false)
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )

            notificationManager.createNotificationChannel(channel)
        }
    }
}







