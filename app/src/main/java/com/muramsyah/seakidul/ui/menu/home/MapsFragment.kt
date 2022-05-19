/*
 * Copyright (c) 2022.
 * Created By M Ramdhan Syahputra on 16-05-2022
 * Github : https://github.com/ramdhanjr11
 * LinkedIn : https://www.linkedin.com/in/ramdhanjr11
 * Instagram : ramdhan.official
 */

package com.muramsyah.seakidul.ui.menu.home

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.muramsyah.seakidul.R
import com.muramsyah.seakidul.utils.ActivityHelper.checkPermission
import com.muramsyah.seakidul.utils.ActivityHelper.showNotice
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
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
    }

    private fun showEvacuationsLocation() {
        val evacuateLocation1 = LatLng(-6.983510693810596, 106.54458191201958)
        val evacuateLocation2 = LatLng(-6.988733173226074, 106.5505967207707)
        val evacuateLocation3 = LatLng(-6.9889613085610405, 106.55329223155792)

        mMap.addMarker(MarkerOptions().position(evacuateLocation1).title(getString(R.string.title_evacuate_location_1)))
        mMap.addMarker(MarkerOptions().position(evacuateLocation2).title(getString(R.string.title_evacuate_location_2)))
        mMap.addMarker(MarkerOptions().position(evacuateLocation3).title(getString(R.string.title_evacuate_location_3)))
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
                context?.let { showNotice(it) }
                requestPermissionForLocation()
            }
        }
    }

    private fun getUserLocation() {
        if (
            checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, activity as AppCompatActivity) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, activity as AppCompatActivity)
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    showUserMarker(location)
                } else {
                    Toast.makeText(context, getString(R.string.title_location_not_found), Toast.LENGTH_SHORT).show()
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
                .title(getString(R.string.title_your_location))
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
    }
}