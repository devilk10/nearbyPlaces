package com.ketansa.nearbyme

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.ketansa.nearbyme.ui.NearbyPlacesScreen
import com.ketansa.nearbyme.ui.theme.NearbyMeTheme

class MainActivity : ComponentActivity() {
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            NearbyMeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LocationScreen()
                }
            }
        }
    }

    @Composable
    private fun LocationScreen() {
        var latitude by remember { mutableDoubleStateOf(12.971599) }
        var longitude by remember { mutableDoubleStateOf(77.594566) }

        // Check and request location permissions
        if (checkPermissions()) {
            // Check if location is enabled
            if (isLocationEnabled()) {
                // Get last location
                LaunchedEffect(true) {
                    getLastLocation { location ->
                        latitude = location?.latitude ?: 12.971599
                        longitude = location?.longitude ?: 77.594566
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Log.d("TAG-imptan", "LocationScreen: $latitude, $longitude")
                    NearbyPlacesScreen(AppContainer.nearbyPlacesVM, latitude, longitude)
                }
            } else {
                Toast.makeText(this, "Please turn on your location...", Toast.LENGTH_LONG).show()
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        } else {
            // Permissions are not granted, request them
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(callback: (Location?) -> Unit) {
        // Get the last location from FusedLocationClient
        mFusedLocationClient.lastLocation
            .addOnCompleteListener(this) { task ->
                val location = task.result
                if (location == null) {
                    requestNewLocationData(callback)
                } else {
                    callback(location)
                }
            }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData(callback: (Location?) -> Unit) {
        // Initializing LocationRequest object
        val mLocationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 5000
            fastestInterval = 0
            numUpdates = 1
        }

        // Setting LocationRequest on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val mLastLocation = locationResult.lastLocation
                callback(mLastLocation)
            }
        }, Looper.myLooper())
    }

    private fun checkPermissions(): Boolean {
        // Check if location permissions are granted
        return (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermissions() {
        // Request location permissions
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permissions granted, get last location
                getLastLocation { location ->
                    // Handle location result if needed
                }
            }
        }.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun isLocationEnabled(): Boolean {
        // Check if location services are enabled
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NearbyMeTheme {
//        Greeting("Android")
    }
}