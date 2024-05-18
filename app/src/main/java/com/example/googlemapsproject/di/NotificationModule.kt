package com.example.googlemapsproject.di

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.googlemapsproject.MainActivity
import com.example.googlemapsproject.R
import com.example.googlemapsproject.util.Constants.ACTION_NAVIGATE_TO_FRAGMENT
import com.example.googlemapsproject.util.Constants.NOTIFICATION_CHANNEL_ID
import com.example.googlemapsproject.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.googlemapsproject.util.Constants.PENDING_INTENT_ID
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped


@Module
@InstallIn(ServiceComponent::class)
object NotificationModule {

    @ServiceScoped
    @Provides
    private fun providePendingIntent(
        @ApplicationContext context: Context
    ): PendingIntent {
        return PendingIntent.getActivity(
            context, PENDING_INTENT_ID, Intent(context, MainActivity::class.java).apply {
                this.action = ACTION_NAVIGATE_TO_FRAGMENT
            }, PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context, pendingIntent: PendingIntent
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).setContentTitle(
            NOTIFICATION_CHANNEL_NAME
        ).setContentText("Running...").setSmallIcon(R.drawable.baseline_directions_run_24)
            .setOngoing(true).setAutoCancel(false).setContentIntent(pendingIntent)
    }


    @ServiceScoped
    @Provides
    fun provideNotificationManger(
        @ApplicationContext context: Context
    ): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

}