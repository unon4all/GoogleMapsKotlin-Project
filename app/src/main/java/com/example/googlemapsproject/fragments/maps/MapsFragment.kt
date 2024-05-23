package com.example.googlemapsproject.fragments.maps

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.googlemapsproject.R
import com.example.googlemapsproject.databinding.FragmentMapsBinding
import com.example.googlemapsproject.fragments.maps.MapUtils.calculateDistance
import com.example.googlemapsproject.fragments.maps.MapUtils.calculateElapsedTime
import com.example.googlemapsproject.fragments.maps.MapUtils.setCameraPosition
import com.example.googlemapsproject.model.Result
import com.example.googlemapsproject.service.TrackerService
import com.example.googlemapsproject.util.Constants.ACTION_SERVICE_START
import com.example.googlemapsproject.util.Constants.ACTION_SERVICE_STOP
import com.example.googlemapsproject.util.ExtensionFunctions.disable
import com.example.googlemapsproject.util.ExtensionFunctions.enable
import com.example.googlemapsproject.util.ExtensionFunctions.hide
import com.example.googlemapsproject.util.ExtensionFunctions.show
import com.example.googlemapsproject.util.Permissions.hasBackgroundLocationPermission
import com.example.googlemapsproject.util.Permissions.requestBackgroundLocationPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Polyline
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private var startTime = 0L
    private var stopTime = 0L

    private lateinit var map: GoogleMap

    private var locationList = mutableListOf<LatLng>()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    val started = MutableLiveData(false)

    private val polyLineList = mutableListOf<Polyline>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {


        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.tracking = this

        binding.startButton.setOnClickListener {
            onStartButtonClicked()
        }

        binding.stopButton.setOnClickListener {
            onStopButtonClicked()
        }

        binding.resetButton.setOnClickListener {
            onResetButtonClicked()
        }

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        return binding.root
    }

    private fun onResetButtonClicked() {
        mapReset()
    }

    private fun onStopButtonClicked() {
        stopForeGroundService()
        binding.stopButton.hide()
        binding.startButton.show()
    }

    private fun stopForeGroundService() {
        binding.startButton.disable()
        sendActionCommandToService(ACTION_SERVICE_STOP)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    @SuppressLint("MissingPermission", "PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.isMyLocationEnabled = true
        map.setOnMyLocationButtonClickListener(this)
        map.uiSettings.apply {
            isZoomControlsEnabled = false
            isZoomGesturesEnabled = false
            isRotateGesturesEnabled = false
            isTiltGesturesEnabled = false
            isCompassEnabled = false
            isScrollGesturesEnabled = false
        }

        observeTrackerService()
    }

    private fun observeTrackerService() {
        TrackerService.locationList.observe(viewLifecycleOwner) {
            if (it != null) {
                locationList = it
                if (locationList.size > 1) {
                    binding.stopButton.enable()
                }
                drawPolyline()
                followPolyline()
            }
        }

        TrackerService.startTime.observe(viewLifecycleOwner) {
            startTime = it
        }

        TrackerService.stopTime.observe(viewLifecycleOwner) {
            stopTime = it
            if (stopTime != 0L) {
                showBiggerPicture()
                displayResults()
            }
        }

        TrackerService.started.observe(viewLifecycleOwner) {
            started.value = it
        }
    }

    private fun showBiggerPicture() {
        val bounds = LatLngBounds.builder()
        for (location in locationList) {
            bounds.include(location)
        }

        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100), 2000, null)
    }

    private fun displayResults() {
        val result = Result(
            calculateDistance(locationList), calculateElapsedTime(startTime, stopTime)
        )

        lifecycleScope.launch {
            delay(2500)
            val direction = MapsFragmentDirections.actionMapsFragmentToResultFragment(
                result
            )

            findNavController().navigate(direction)
            binding.startButton.apply {
                hide()
                enable()
            }

            binding.stopButton.hide()
            binding.resetButton.show()
        }
    }

    private fun drawPolyline() {
        val polyline = map.addPolyline(com.google.android.gms.maps.model.PolylineOptions().apply {
            width(10f)
            color(ContextCompat.getColor(requireContext(), R.color.black))
            jointType(com.google.android.gms.maps.model.JointType.ROUND)
            startCap(com.google.android.gms.maps.model.RoundCap())
            endCap(com.google.android.gms.maps.model.RoundCap())
            addAll(locationList)
        })

        polyLineList.add(polyline)
    }

    private fun followPolyline() {
        if (locationList.isNotEmpty()) {
            map.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    setCameraPosition(locationList.last())
                ), 1000, null
            )
        }
    }

    private fun onStartButtonClicked() {
        if (hasBackgroundLocationPermission(requireContext())) {
            startCountDown()
            binding.startButton.disable()
            binding.startButton.hide()
            binding.stopButton.show()
        } else {
            requestBackgroundLocationPermission(this)
        }
    }

    private fun startCountDown() {
        binding.timerTextView.show()
        binding.stopButton.disable()
        val timer: CountDownTimer = object : CountDownTimer(4000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val currentSecond = millisUntilFinished / 1000
                if (currentSecond.toString() == "0") {
                    binding.timerTextView.text = "GO"
                    binding.timerTextView.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.black
                        )
                    )
                } else {
                    binding.timerTextView.text = currentSecond.toString()
                    binding.timerTextView.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.red
                        )
                    )
                }
            }

            override fun onFinish() {
                sendActionCommandToService(ACTION_SERVICE_START)
                binding.timerTextView.hide()
            }
        }
        timer.start()
    }

    private fun sendActionCommandToService(action: String) {
        Intent(
            requireContext(), TrackerService::class.java
        ).apply {
            this.action = action
            requireContext().startService(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMyLocationButtonClick(): Boolean {
        binding.hintTextView.animate().alpha(0f).duration = 1500
        lifecycleScope.launch {
            delay(2500)
            binding.hintTextView.hide()
            binding.startButton.show()
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestBackgroundLocationPermission(this)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        onStartButtonClicked()
    }

    @SuppressLint("MissingPermission")
    private fun mapReset() {

        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
            val lastKnownLocation = LatLng(it.result.latitude, it.result.longitude)

            for (polyline in polyLineList) {
                polyline.remove()
            }

            map.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    setCameraPosition(lastKnownLocation)
                )
            )

            locationList.clear()
            binding.resetButton.hide()
            binding.startButton.show()
        }
    }

}


