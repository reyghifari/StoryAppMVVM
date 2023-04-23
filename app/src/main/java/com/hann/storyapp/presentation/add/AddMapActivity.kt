package com.hann.storyapp.presentation.add

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.hann.storyapp.R
import com.hann.storyapp.databinding.ActivityAddMapBinding
import com.hann.storyapp.presentation.add.AddStoryActivity.Companion.EXTRA_LATITUDE
import com.hann.storyapp.presentation.add.AddStoryActivity.Companion.EXTRA_LONGTITUDE
import java.io.IOException
import java.lang.IndexOutOfBoundsException
import java.util.*

class AddMapActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener,
    GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener {

    private lateinit var binding: ActivityAddMapBinding
    private lateinit var mMap : GoogleMap
    private lateinit var mapView: MapView
    private val DEFAULT_ZOOM = 15f
    private var mapViewBundle : Bundle? = null
    private var fusedLocationProviderClient : FusedLocationProviderClient? = null
    private var latitude : String = "0.0"
    private var lngtitude : String = "0.0"
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMapBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mapView = findViewById(R.id.viewMap)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the permission if it has not been granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission already granted, proceed with the operation that requires the permission
            // ...
            mapView.onCreate(mapViewBundle)
            mapView.getMapAsync(this)
            getCurrentLocation()
        }

        binding.btnLoc.setOnClickListener {
            val intent = Intent (this, AddStoryActivity::class.java)
            intent.putExtra(EXTRA_LATITUDE, latitude)
            intent.putExtra(EXTRA_LONGTITUDE, lngtitude)
            startActivity(intent)
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mapView.onCreate(mapViewBundle)
                mapView.getMapAsync(this)
                getCurrentLocation()
            } else {
               finish()
            }
        }
    }

    private fun getCurrentLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        try {
            @SuppressLint("MissingPermission")
            val location = fusedLocationProviderClient!!.lastLocation

            location.addOnCompleteListener(object : OnCompleteListener<Location> {
                override fun onComplete(p0: Task<Location>){
                    if (p0.isSuccessful){
                        val currentLocation = p0.result as Location?
                        if (currentLocation != null){
                            moveCamera(
                                LatLng(currentLocation.latitude, currentLocation.longitude)
                            )
                        }
                    }
                }
            })

        }catch (e: Exception){
            Log.e("TAG","Security Excepton")
        }
    }


    private fun moveCamera(latLng: LatLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,DEFAULT_ZOOM))
    }

    override fun onMapReady(p0: GoogleMap) {
        mapView.onResume()
        mMap = p0

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED){
            return
        }
        mMap.isMyLocationEnabled = true

        mMap.setOnCameraMoveListener(this)
        mMap.setOnCameraMoveStartedListener(this)
        mMap.setOnCameraIdleListener(this)
    }

    override fun onLocationChanged(location: Location) {
        latitude = location.latitude.toString()
        lngtitude =location.longitude.toString()

        setLocation()
    }

    override fun onCameraMove() {

    }

    override fun onCameraMoveStarted(p0: Int) {

    }

    override fun onCameraIdle() {
        val cameraPosition = mMap.cameraPosition
        val latLng = cameraPosition.target

        // Get the latitude and longitude strings
        latitude = latLng.latitude.toString()
        lngtitude = latLng.longitude.toString()
        setLocation()
    }

    private fun setLocation() {
        binding.tvAdd.text = "Latitude: $latitude \nLongtitude: $lngtitude"

    }

}