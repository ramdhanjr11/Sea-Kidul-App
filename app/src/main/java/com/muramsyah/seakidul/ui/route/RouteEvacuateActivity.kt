/*
 * Copyright (c) 2022.
 * Created By M Ramdhan Syahputra on 16-05-2022
 * Github : https://github.com/ramdhanjr11
 * LinkedIn : https://www.linkedin.com/in/ramdhanjr11
 * Instagram : ramdhan.official
 */

package com.muramsyah.seakidul.ui.route

import android.Manifest
import android.content.IntentSender
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.muramsyah.seakidul.R
import com.muramsyah.seakidul.databinding.ActivityRouteEvacuateBinding
import com.muramsyah.seakidul.utils.ActivityHelper
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class RouteEvacuateActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG: String = RouteEvacuateActivity::class.java.simpleName
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityRouteEvacuateBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRouteEvacuateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(map: GoogleMap) {
        val pelabuhanRatu = LatLng(-6.988640004677339, 106.55066410614342)
        mMap = map
        map.uiSettings.isMapToolbarEnabled = true
        map.uiSettings.isZoomControlsEnabled = true
        map.setPadding(0, 0,0, 150)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(pelabuhanRatu, 15f))
        showEvacuationsLocation()
        getUserLocation()
        createLocationRequest()
        createLocationCallback()
        startLocationUpdates()
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                for (location in result.locations) {
                    Log.d(TAG, "onLocationResult: ${location.latitude}, ${location.longitude}")
                }
            }
        }
    }

    private fun showEvacuationsLocation() {
        val evacuateLocation1 = LatLng(-6.983510693810596, 106.54458191201958)
        val evacuateLocation2 = LatLng(-6.988733173226074, 106.5505967207707)
        val evacuateLocation3 = LatLng(-6.9889613085610405, 106.55329223155792)

        mMap.addMarker(MarkerOptions().position(evacuateLocation1).title("Lokasi Evakuasi 1"))
        mMap.addMarker(MarkerOptions().position(evacuateLocation2).title("Lokasi Evakuasi 2"))
        mMap.addMarker(MarkerOptions().position(evacuateLocation3).title("Lokasi Evakuasi 3"))
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                getUserLocation()
            }
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                getUserLocation()
            }
            else -> {
                this.let { ActivityHelper.showNotice(it) }
                requestPermissionForLocation()
            }
        }
    }

    private val resolutionLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                Log.i(TAG, "onActivityResult: All location settings are satisfied.")
            }
            RESULT_CANCELED -> {
                Toast.makeText(
                    this,
                    "Kamu harus mengaktifkan GPS untuk menggunakan aplikasi ini!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getUserLocation() {
        if (
            ActivityHelper.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, this) &&
            ActivityHelper.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, this)
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    showUserMarker(location)
                } else {
                    Toast.makeText(this, "Lokasi tidak ditemukan. Coba lagi!", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            requestPermissionForLocation()
        }
    }

    private fun requestPermissionForLocation() {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun showUserMarker(location: Location) {
        val userLocation = LatLng(location.latitude, location.longitude)
        mMap.addMarker(
            MarkerOptions()
                .position(userLocation)
                .title("Lokasi kamu")
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(1)
            maxWaitTime = TimeUnit.SECONDS.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        client.checkLocationSettings(builder.build())
            .addOnSuccessListener {
                getUserLocation()
            }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        resolutionLauncher.launch(
                            IntentSenderRequest.Builder(exception.resolution).build()
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        Toast.makeText(this, sendEx.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun startLocationUpdates() {
        try {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (exception: SecurityException) {
            Log.e(TAG, "startLocationUpdates Error: ${exception.message}", )
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }
}