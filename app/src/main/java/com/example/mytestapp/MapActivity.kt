package com.example.mytestapp

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Request
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import android.Manifest
import android.location.Location
import com.google.gson.Gson
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Obtain the MapView and call its onCreate() method
        mapView = findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)

        // Get the GoogleMap object asynchronously
        mapView.getMapAsync(this)

        // Check if location permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            // Get the FusedLocationProviderClient
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        } else{
            // Permission is not granted, request permission
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION)
        }

        // Call this function from the location callback
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                Log.d("location", location.toString())

                // Use a Coroutine to get the place ID in the background
                GlobalScope.launch {
                    val placeId = getPlaceId(location)
                    Log.d("placeId", placeId)
                    //this is the place ID of my current location but sometimes when i try to get the route it fails
                    //so im gonna use for my current location (origin) the location of UA

                    // Get the route between the user's location and Aveiro
                    val url = "https://maps.googleapis.com/maps/api/directions/json?origin=place_id:ChIJK9obTqqiIw0RhVRHSWeXC9c&destination=place_id:ChIJYyaa4skLIw0RUFmQ5L3rAAU&mode=driving&key=AIzaSyBB63oqZMQhZxzzmFRT1yQffSWkM6RDFIU"
                    val request = JsonObjectRequest(Request.Method.GET, url, null,
                        { response ->
                            // Parse the JSON response and draw the route on the map
                            Log.d("responseDirections", response.toString())
                            val route = response.getJSONArray("routes").getJSONObject(0)
                            val legs = route.getJSONArray("legs").getJSONObject(0)
                            val steps = legs.getJSONArray("steps")
                            val polylineOptions = PolylineOptions()
                            for (i in 0 until steps.length()) {
                                val points = steps.getJSONObject(i).getJSONObject("polyline").getString("points")
                                polylineOptions.addAll(PolyUtil.decode(points))
                            }
                            googleMap.addPolyline(polylineOptions)
                        },
                        { error ->
                            Log.e("MapActivity", "Error getting directions: ${error.message}")
                        })
                    Volley.newRequestQueue(this@MapActivity).add(request)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, initialize fusedLocationClient
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            } else {
                // Permission denied, handle accordingly (e.g. show an error message)
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Set the map type
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        GlobalScope.launch {
            // Add a marker at a specific location
            val url = "https://maps.googleapis.com/maps/api/geocode/json?address=Agueda&key=AIzaSyBB63oqZMQhZxzzmFRT1yQffSWkM6RDFIU"
            val response = URL(url).readText()
            val json = JSONObject(response)
            val results = json.getJSONArray("results")
            Log.d("results", results.toString())
            val location =
                results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location")
            val lat = location.getDouble("lat")
            val lng = location.getDouble("lng")
            val agueda = LatLng(lat, lng)
            withContext(Dispatchers.Main) {
                googleMap.addMarker(MarkerOptions().position(agueda).title("Agueda"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(agueda, 12f))
            }
        }

        // Check if the app has permission to access the user's location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, request location updates
            val locationRequest = LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        } else {
            // Permission is not granted, request permission
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION)
        }
    }

    // Define a function to get the place ID from the location
    fun getPlaceId(location: Location): String {
        val url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=${location.latitude},${location.longitude}&key=AIzaSyBB63oqZMQhZxzzmFRT1yQffSWkM6RDFIU"
        val response = URL(url).readText()
        val json = JSONObject(response)
        val results = json.getJSONArray("results")
        return results.getJSONObject(0).getString("place_id")
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
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