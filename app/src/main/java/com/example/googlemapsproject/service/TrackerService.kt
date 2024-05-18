package com.example.googlemapsproject.service

import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.googlemapsproject.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.googlemapsproject.util.Constants.ACTION_STOP_SERVICE

class TrackerService : LifecycleService() {

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
}