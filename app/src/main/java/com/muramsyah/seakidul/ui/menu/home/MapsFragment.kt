/*
 * Copyright (c) 2022.
 * Created By M Ramdhan Syahputra on 16-05-2022
 * Github : https://github.com/ramdhanjr11
 * LinkedIn : https://www.linkedin.com/in/ramdhanjr11
 * Instagram : ramdhan.official
 */

package com.muramsyah.seakidul.ui.menu.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.muramsyah.seakidul.R

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

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
    }

    override fun onMapReady(map: GoogleMap) {
        val pelabuhanRatu = LatLng(-6.988640004677339, 106.55066410614342)
        mMap = map
        map.uiSettings.isMapToolbarEnabled = true
        map.uiSettings.isZoomControlsEnabled = true
        map.setPadding(0, 0,0, 150)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(pelabuhanRatu, 15f))
        showEvacuationsLocation()
    }

    private fun showEvacuationsLocation() {
        val evacuateLocation1 = LatLng(-6.983510693810596, 106.54458191201958)
        val evacuateLocation2 = LatLng(-6.988733173226074, 106.5505967207707)
        val evacuateLocation3 = LatLng(-6.9889613085610405, 106.55329223155792)

        mMap.addMarker(MarkerOptions().position(evacuateLocation1).title("Lokasi Evakuasi 1"))
        mMap.addMarker(MarkerOptions().position(evacuateLocation2).title("Lokasi Evakuasi 2"))
        mMap.addMarker(MarkerOptions().position(evacuateLocation3).title("Lokasi Evakuasi 3"))
    }
}