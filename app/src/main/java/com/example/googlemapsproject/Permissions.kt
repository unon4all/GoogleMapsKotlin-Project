package com.example.googlemapsproject

import android.Manifest
import android.content.Context
import androidx.fragment.app.Fragment
import com.example.googlemapsproject.Constants.PERMISSION_REQUEST_CODE
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

}