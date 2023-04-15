package com.example.mytestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Obtain the MapView and call its onCreate() method
        mapView = findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)

        // Get the GoogleMap object asynchronously
        mapView.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Set the map type
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        // Add a marker at a specific location
        val latLng = LatLng(37.7749, -122.4194)
        googleMap.addMarker(MarkerOptions().position(latLng).title("San Francisco"))

        // Move the camera to the marker
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
}