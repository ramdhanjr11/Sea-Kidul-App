/*
 * Copyright (c) 2022.
 * Created By M Ramdhan Syahputra on 16-05-2022
 * Github : https://github.com/ramdhanjr11
 * LinkedIn : https://www.linkedin.com/in/ramdhanjr11
 * Instagram : ramdhan.official
 */

package com.muramsyah.seakidul.ui.route

import DijkstraAlgorithm
import Node
import android.Manifest
import android.content.IntentSender
import android.graphics.Color
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
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.muramsyah.seakidul.R
import com.muramsyah.seakidul.databinding.ActivityRouteEvacuateBinding
import com.muramsyah.seakidul.utils.ActivityHelper
import com.muramsyah.seakidul.utils.JsonHelper
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import searchCloseDistance
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class RouteEvacuateActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG: String = RouteEvacuateActivity::class.java.simpleName
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityRouteEvacuateBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var allLatLng = ArrayList<LatLng>()
    private var allClosetsLatLng = ArrayList<LatLng>()
    private var boundsBuilder = LatLngBounds.Builder()

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
                    val lastLatLng = LatLng(location.latitude, location.longitude)

                    //draw polyline
//                    allLatLng.add(lastLatLng)
//                    mMap.addPolyline(
//                        PolylineOptions()
//                            .color(Color.CYAN)
//                            .width(10f)
//                            .addAll(allLatLng)
//                    )
//
//                    boundsBuilder.include(lastLatLng)
//                    val bounds = boundsBuilder.build()
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 64))
                }
            }
        }
    }

    private fun showEvacuationsLocation() {
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
        findClosestRoute(location)
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

    private fun findClosestRoute(location: Location) {
        val evacuateLocations = listOf(
            Node(evacuateLocation1.latitude, evacuateLocation1.longitude),
            Node(evacuateLocation2.latitude, evacuateLocation2.longitude),
            Node(evacuateLocation3.latitude, evacuateLocation3.longitude)
        )
        val calculateClosestEvacuates = evacuateLocations.map {
            searchCloseDistance(evacuateLocations, location.latitude, location.longitude)
        }
        val resultExtractCalculate = HashMap<Int, Double>()
        calculateClosestEvacuates.forEach { it -> it.entries.forEach { resultExtractCalculate[it.key] = it.value } }
        val filterClosestEvacuateLocation = resultExtractCalculate.toList().sortedBy { it.second }
        val resultFilterEvacuateLocation = evacuateLocations[filterClosestEvacuateLocation[0].first]

        val objectPlara = JSONObject(JsonHelper(this).parsingFileToString("plara.json").toString())
        val userLocation = Node(lat = location.latitude, long = location.longitude)
        val dijkstraAlgorithm = DijkstraAlgorithm.Builder(objectPlara)
            .setStartOrigin("v1")
            .setEndDestination(
                when (resultFilterEvacuateLocation) {
                    nodeEvacuateLocation1 -> "v8"
                    nodeEvacuateLocation2 -> "v21"
                    nodeEvacuateLocation3 -> "v29"
                    else -> "null"
                }
            )
            .setUserNode(userLocation = userLocation)
            .create()

        filteringNodesToShowToMap(dijkstraAlgorithm)

        //draw polyline
        mMap.addPolyline(
            PolylineOptions()
                .color(Color.GREEN)
                .width(10f)
                .addAll(allClosetsLatLng.toSet())
        )

        boundsBuilder.include(LatLng(location.latitude, location.longitude))
        val bounds = boundsBuilder.build()
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 64))

        Log.d(TAG, "Shortest Path Result: ${dijkstraAlgorithm.shortestPath}")
        Log.d(TAG, "Routes Result: ${dijkstraAlgorithm.routes}")
    }

    private fun filteringNodesToShowToMap(dijkstraAlgorithm: DijkstraAlgorithm) {
        val shortestPath= dijkstraAlgorithm.shortestPath
        val mapNodes = ArrayList<Pair<String, String>>()
        for (i in shortestPath.indices) {
            if (i != shortestPath.size - 1) {
                mapNodes.add(
                    Pair(
                        shortestPath[i],
                        shortestPath[i+1]
                    )
                )
            }
        }

        val routeResult = dijkstraAlgorithm.routes
        mapNodes.forEach { value ->
            routeResult.filterKeys { it == value }.values.flatten().forEach {
                allClosetsLatLng.add(
                    LatLng(it.lat, it.long)
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }

    companion object {
        private val evacuateLocation1 = LatLng(-6.983510693810596, 106.54458191201958)
        private val evacuateLocation2 = LatLng(-6.988733173226074, 106.5505967207707)
        private val evacuateLocation3 = LatLng(-6.9889613085610405, 106.55329223155792)
        private val nodeEvacuateLocation1 = Node(evacuateLocation1.latitude, evacuateLocation1.longitude)
        private val nodeEvacuateLocation2 = Node(evacuateLocation2.latitude, evacuateLocation2.longitude)
        private val nodeEvacuateLocation3 = Node(evacuateLocation3.latitude, evacuateLocation3.longitude)
    }
}