package com.example.googlemapsproject.fragments

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import java.text.DecimalFormat

object MapUtils {

    fun setCameraPosition(location: LatLng): CameraPosition {
        return CameraPosition.Builder().target(location).zoom(18f).build()
    }

    fun calculateElapsedTime(startTime: Long, stopTime: Long): String {
        val elapsedTime = stopTime - startTime
        val seconds = (elapsedTime / 1000).toInt() % 60
        val minutes = (elapsedTime / (1000 * 60) % 60).toInt()
        val hours = (elapsedTime / (1000 * 60 * 60) % 24).toInt()
        return "$hours:$minutes:$seconds"
    }

    fun calculateDistance(locationList: MutableList<LatLng>): String {
        if (locationList.size > 1) {
            val meters = SphericalUtil.computeDistanceBetween(locationList[0], locationList.last())

            val kilometers = meters / 1000

            return DecimalFormat("#.##").format(kilometers)
        }
        return "0.00"
    }
}