package com.example.safetrip.dashboard

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.safetrip.DashboardMain
import com.example.safetrip.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.example.safetrip.databinding.ActivitySafeTripLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_safe_trip_location.*

class SafeTripLocation : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivitySafeTripLocationBinding
    private lateinit var lastSafeLocate: SafeTripLocation
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var preferences: SharedPreferences
    private lateinit var database: DatabaseReference


    companion object{
        private const val LOCATION_REQUEST_CODE = 1
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        binding = ActivitySafeTripLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        preferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE)
        val driverInfo = preferences.getString("DRIVER_INFORMATION", "NULL").toString()

        database = FirebaseDatabase.getInstance().reference
        database.child("Driver/$driverInfo").get().addOnSuccessListener {
            if(it.exists()){
                val driverName = it.child("driverName").value
                val driverPlate = it.child("driverPlate").value
                val driverNumber = it.child("driverNumber").value

                txtDriverName.text = driverName.toString()
                txtDriverPlate.text = driverPlate.toString()
                txtDriverNum.text = driverNumber.toString()
            }
        }

        val btnDrop = findViewById<Button>(R.id.btnDrop)
        btnDrop.setOnClickListener()
        {
            startActivity(Intent(this, RideComplete::class.java))
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                this, R.raw.style_json))

        mMap.uiSettings.isZoomControlsEnabled = false
        mMap.setOnMarkerClickListener(this)
        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { SafeTripLocation ->
            if (SafeTripLocation != null)
            {
                lastSafeLocate = SafeTripLocation()
                val currentLatLong = LatLng(SafeTripLocation.latitude, SafeTripLocation.longitude)
                placeMarkerOnMap(currentLatLong)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 19f))
            }
        }
    }

    private fun placeMarkerOnMap(currentLatLong: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("$currentLatLong")

        mMap.addMarker(markerOptions)

    }

    override fun onMarkerClick(p0: Marker): Boolean = false
}

