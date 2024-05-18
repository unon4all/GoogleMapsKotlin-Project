package com.example.googlemapsproject.util

import android.Manifest
import android.content.Context
import androidx.fragment.app.Fragment
import com.example.googlemapsproject.util.Constants.PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE
import com.example.googlemapsproject.util.Constants.PERMISSION_REQUEST_CODE
import com.vmadalin.easypermissions.EasyPermissions

object Permissions {

    fun hasLocationPermission(context: Context) =
        EasyPermissions.hasPermissions(context, Manifest.permission.ACCESS_FINE_LOCATION)


    fun requestLocationPermission(fragment: Fragment) {

        EasyPermissions.requestPermissions(
            fragment,
            "This application cannot work without Location Permission",
            PERMISSION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    }

    fun hasBackgroundLocationPermission(context: Context): Boolean {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
            return true
        }
        return EasyPermissions.hasPermissions(
            context, Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
    }

    fun requestBackgroundLocationPermission(fragment: Fragment) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            return EasyPermissions.requestPermissions(
                fragment,
                "This application cannot work without Background Location Permission",
                PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

}