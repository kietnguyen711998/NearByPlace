package com.example.nearbyplace.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.nearbyplace.ApiClient
import com.example.nearbyplace.NearbyApi
import com.example.nearbyplace.R
import com.example.nearbyplace.model.nearby.NearByPlace
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var TAG: String = ""

    private var mLocationRequest: LocationRequest? = null
    private var mLastLocation: Location? = null
    private var mCurrLocationMarker: Marker? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    //    lateinit var keySearch: String
    var keys: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        location
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mLocationRequest = LocationRequest()
        mLocationRequest?.interval = 120000 // two minute interval
        mLocationRequest?.fastestInterval = 120000
        mLocationRequest?.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                //Location Permission already granted
                mFusedLocationClient?.requestLocationUpdates(
                    mLocationRequest, mLocationCallback,
                    Looper.myLooper()
                )
                mMap?.isMyLocationEnabled = true
            } else {
                //Request Location Permission
                checkLocationPermission()
            }
        } else {
            mFusedLocationClient?.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
            )
            mMap?.isMyLocationEnabled = true
        }
    }

    var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.size > 0) {
                //The last location in the list is the newest
                val location = locationList[locationList.size - 1]
                mLastLocation = location
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker?.remove()
                }

                //move map camera
                val latLng = LatLng(location.latitude, location.longitude)
                val cameraPosition =
                    CameraPosition.Builder().target(LatLng(latLng.latitude, latLng.longitude))
                        .zoom(16f).build()
                mMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton("OK") { dialogInterface, i -> //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(
                            this@MapsActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_LOCATION
                        )
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        }
    }

    private fun showNearbyPlaces(keys: String) {

        if (this.keys.equals("hotel")) {
                //markerOptions.icon(bitmapDescriptorFromVector(mContext, R.drawable.ic_hotel1))
        }
        if (this.keys.equals("hospital")) {
//                markerOptions.icon(bitmapDescriptorFromVector(mContext, R.drawable.ic_hospital1))
        }
        if (this.keys.equals("restaurant")) {
//                markerOptions.icon(bitmapDescriptorFromVector(mContext, R.drawable.ic_restaurant1))
        }
        if (this.keys.equals("school")) {
//                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//                markerOptions.icon(bitmapDescriptorFromVector(mContext, R.drawable.ic_school1))
        } else {
            Log.d(TAG, "showNearbyPlaces: " + "Dont show")
        }
//            mMap.addMarker(markerOptions)
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(16f))

    }

    private val location: Unit
        private get() {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val criteria = Criteria()
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1000
                )
                return
            }
            mLastLocation = locationManager.getBestProvider(criteria, false)?.let {
                locationManager.getLastKnownLocation(
                    it
                )
            }
            val intent = getIntent()
            keys = intent.getStringExtra("keys")
            keys?.let { showNearbyPlaces(it) }
            val lng = mLastLocation?.longitude
            val lat = mLastLocation?.latitude
            val slng = lng?.let { java.lang.Double.toString(it) }
            val slat = lat?.let { java.lang.Double.toString(it) }
            val location = "$slat,$slng"
            val radius = "10000"
            //var type = keys?.let { showNearbyPlaces(it) }
//            var type = keys.let {
//                if (it != null) {
//                    showNearbyPlaces(it)
//                }
//            }

//            var type = "hotel"
            var type = "school"
            //var type = "hospital"


            val key = "AIzaSyDtxS6znDp9TzYPYdV8XwptR-ARnFHKRCs"
            val nearbyApi: NearbyApi = ApiClient.getClient()!!.create(NearbyApi::class.java)
            val call: Call<NearByPlace> =
                nearbyApi.getDetails(location, radius, type, key)
            call.enqueue(object : Callback<NearByPlace> {
                override fun onResponse(
                    call: Call<NearByPlace>,
                    response: Response<NearByPlace>
                ) {
                    Log.d("Res123", response.body()?.results?.get(0)?.name ?: "")
                    Log.d("Res123", response.body()?.results?.get(0)?.id ?: "")
                    Log.d("Res123", response.body()?.results?.get(0)?.vicinity ?: "")
                    mMap?.clear()
                    // This loop will go through all the results and add marker on each location.
                    for (i in response.body()?.results?.indices!!) {
                        val lat = response.body()?.results?.get(i)?.geometry?.location?.lat
                        val lng = response.body()?.results?.get(i)?.geometry?.location?.lng
                        val placeName = response.body()?.results?.get(i)?.name
                        val vicinity = response.body()?.results?.get(i)?.vicinity
                        val markerOptions = MarkerOptions()
                        val latLng = lat?.let { lng?.let { it1 -> LatLng(it, it1) } }
                        // Position of Marker on Map
                        if (latLng != null) {
                            markerOptions.position(latLng)
                        }
                        // Adding Title to the Marker
                        markerOptions.title("$placeName : $vicinity")
                        // Adding colour to the marker
                        markerOptions.icon(
                            BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_RED
                            )
                        )
                        // Adding Marker to the Camera.
                        val m = mMap?.addMarker(markerOptions)
                        // move map camera
                        mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                        mMap?.animateCamera(CameraUpdateFactory.zoomTo(11f))

                    }
                }

                override fun onFailure(
                    call: Call<NearByPlace>,
                    t: Throwable
                ) {
                    Log.e("Response111", "Failure")
                }
            })
        }

    companion object {
        const val MY_PERMISSIONS_REQUEST_LOCATION = 99
    }
}