package com.example.googlemapsproject.fragments.permissions

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.googlemapsproject.R
import com.example.googlemapsproject.databinding.FragmentPermissionBinding
import com.example.googlemapsproject.util.Constants.PERMISSION_LOCATION_REQUEST_CODE
import com.example.googlemapsproject.util.Constants.PERMISSION_POST_NOTIFICATIONS_REQUEST_CODE
import com.example.googlemapsproject.util.Permissions.hasLocationPermission
import com.example.googlemapsproject.util.Permissions.hasPostNotificationsPermission
import com.example.googlemapsproject.util.Permissions.requestLocationPermission
import com.example.googlemapsproject.util.Permissions.requestPostNotificationsPermission
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog


class PermissionFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentPermissionBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPermissionBinding.inflate(inflater, container, false)

        binding.continueButton.setOnClickListener {
            if (hasLocationPermission(requireContext())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (hasPostNotificationsPermission(requireContext())) {
                        findNavController().navigate(R.id.action_permissionFragment_to_mapsFragment)
                    } else {
                        requestPostNotificationsPermission(this)
                    }
                } else {
                    findNavController().navigate(R.id.action_permissionFragment_to_mapsFragment)
                }
            } else {
                requestLocationPermission(this)
            }
        }

        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            if (requestCode == PERMISSION_LOCATION_REQUEST_CODE) {
                requestLocationPermission(this)
            } else if (requestCode == PERMISSION_POST_NOTIFICATIONS_REQUEST_CODE) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPostNotificationsPermission(this)
                }
            }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (requestCode == PERMISSION_LOCATION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPostNotificationsPermission(this)
            } else {
                findNavController().navigate(R.id.action_permissionFragment_to_mapsFragment)
            }
        } else if (requestCode == PERMISSION_POST_NOTIFICATIONS_REQUEST_CODE) {
            findNavController().navigate(R.id.action_permissionFragment_to_mapsFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}