package com.example.nearbyplace.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.*
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.nearbyplace.ApiClient
import com.example.nearbyplace.NearbyApi
import com.example.nearbyplace.R
import com.example.nearbyplace.model.directions.Direction
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
import kotlinx.android.synthetic.main.activity_maps.*
import org.w3c.dom.UserDataHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener,
    GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {

    private var mMap: GoogleMap? = null
    private var geocoder: Geocoder? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLastLocation: Location? = null
    private var mCurrLocationMarker: Marker? = null

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var polylineOptions: PolylineOptions
    private lateinit var dataHandler: UserDataHandler
    private lateinit var directionResults: Direction
    private var place1: MarkerOptions? = null
    private var place2: MarkerOptions? = null
    private var latPlace2: MarkerOptions? = null
    private var latlocatinon1: Double? = null
    private var lnglocatinon1: Double? = null

    private var namePlace1: String? = null

    private var namePlace2: String? = null
    var keys: String? = null
    var addressClick: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        location
        btnRoute?.setOnClickListener {
            locationDirection
        }


    }

    private fun getTheAddress(latitude: Double, longitude: Double) {
        val addresses: List<Address>
        geocoder = Geocoder(this, Locale.getDefault())
        try {
            addresses = geocoder!!.getFromLocation(latitude, longitude, 1)
            val address = addresses[0].getAddressLine(0)
            val city = addresses[0].locality
            val state = addresses[0].adminArea
            val country = addresses[0].countryName
            val postalCode = addresses[0].postalCode
            val knownName = addresses[0].featureName
            Log.d("vvvv", address)
            namePlace1 = address
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.setOnMapLongClickListener(this)
        geocoder = Geocoder(this)
        mLocationRequest = LocationRequest()
        mLocationRequest?.interval = 120000 // two minute interval
        mLocationRequest?.fastestInterval = 120000
        mLocationRequest?.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                //Location Permission already granted
                mFusedLocationClient?.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback,
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

    fun getAddress(latitude: Double, longitude: Double): Address? {
        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(this, Locale.getDefault())
        try {
            addresses = geocoder.getFromLocation(
                latitude,
                longitude,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses[0]
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
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
                mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f))
                val cameraPosition =
                    CameraPosition.Builder().target(LatLng(latLng.latitude, latLng.longitude))
                        .zoom(16f).build()
                place1 = MarkerOptions().position(LatLng(latLng.latitude, latLng.longitude))
                namePlace1

                mMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }
    }

    fun getAddressFromLatLng(context: Context?, latLng: LatLng): String? {
        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(context, Locale.getDefault())
        return try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            addresses[0].getAddressLine(0)
            addresses[1].locality
            addresses[2].adminArea
            addresses[3].countryName
            addresses[4].postalCode
            addresses[5].featureName
        } catch (e: Exception) {
            e.printStackTrace()
            ""
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
//            keys?.let { showNearbyPlaces(it) }
            val lng = mLastLocation?.longitude
            val lat = mLastLocation?.latitude
            val slng = lng?.let { java.lang.Double.toString(it) }
            val slat = lat?.let { java.lang.Double.toString(it) }
            val location = "$slat,$slng"
            val radius = "10000"
            var type: String? = keys
//            var type = "hotel"
            var photoreference = ""
            val sensor = "false"
            var maxheight = ""
            var maxwidth = ""

            val key = "AIzaSyDtxS6znDp9TzYPYdV8XwptR-ARnFHKRCs"
            val nearbyApi: NearbyApi = ApiClient.getClient()!!.create(NearbyApi::class.java)
            val call: Call<NearByPlace> =
                nearbyApi.getDetails(location, radius, type.toString(), key)
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
                        val callimg: Call<NearByPlace> =
                            nearbyApi.getDetailsPhoto(
                                photoreference,
                                sensor,
                                maxheight,
                                maxwidth,
                                key
                            )
                        callimg.enqueue(object : Callback<NearByPlace> {
                            override fun onResponse(
                                call: Call<NearByPlace>,
                                response: Response<NearByPlace>
                            ) {
                                val photoreference = response.body()?.results?.get(i)?.photos?.get(i)?.photoReference
                                val maxheight = response.body()?.results?.get(i)?.photos?.get(i)?.height
                                val maxwidth = response.body()?.results?.get(i)?.photos?.get(i)?.width
                            }

                            override fun onFailure(call: Call<NearByPlace>, t: Throwable) {
                                TODO("Not yet implemented")
                            }
                        })



                        val lat = response.body()?.results?.get(i)?.geometry?.location?.lat
                        val lng = response.body()?.results?.get(i)?.geometry?.location?.lng
                        latlocatinon1 = lat
                        lnglocatinon1 = lng
                        val placeName = response.body()?.results?.get(i)?.name
                        namePlace2 = placeName
                        val vicinity = response.body()?.results?.get(i)?.vicinity
                        val markerOptions = MarkerOptions()
                        val latLng = lat?.let { lng?.let { it1 -> LatLng(it, it1) } }
                        //place2 = latLng?.let { MarkerOptions().position(it) }
                        // Position of Marker on Map
                        if (latLng != null) {
                            markerOptions.position(latLng)
                        }
                        // Adding Title to the Marker
                        markerOptions.title("$placeName : $vicinity")
                        when (vicinity) {
                            "hotel" -> markerOptions.icon(
                                bitmapDescriptorFromVector(this@MapsActivity, R.drawable.ic_hotel)
                            )
                            "hospital" -> markerOptions.icon(
                                bitmapDescriptorFromVector(this@MapsActivity, R.drawable.ic_hospital)
                            )
                            "restaurant" -> markerOptions.icon(
                                bitmapDescriptorFromVector(this@MapsActivity, R.drawable.ic_restaurant)
                            )
                            "school" -> markerOptions.icon(
                                bitmapDescriptorFromVector(this@MapsActivity, R.drawable.ic_school)
                            )
                            else -> markerOptions.icon(
                                BitmapDescriptorFactory.defaultMarker(
                                    BitmapDescriptorFactory.HUE_AZURE
                                )
                            )
                        }
                        // Adding colour to the marker
                        // Adding Marker to the Camera.
                        val m = mMap?.addMarker(markerOptions)
                        // move map camera
                        mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                        mMap?.animateCamera(CameraUpdateFactory.zoomTo(16f))
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


    private val locationDirection: Unit
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
            val lng = mLastLocation?.longitude
            val lat = mLastLocation?.latitude
            val slng = lng?.let { java.lang.Double.toString(it) }
            val slat = lat?.let { java.lang.Double.toString(it) }
            val location = "$slat,$slng"
            val origin = "41 Lê Duẩn, Hải Châu 1, Hải Châu, Đà Nẵng 550000, Việt Nam"
            var destination = "$namePlace2"
            val key = "AIzaSyDtxS6znDp9TzYPYdV8XwptR-ARnFHKRCs"
            //via:16.077329,108.22367|via:16.060475,108.223349
            val waypoints = "via:$location" + "|via:$latlocatinon1+$lnglocatinon1"

            val nearbyApi: NearbyApi = ApiClient.getClient()!!.create(NearbyApi::class.java)
            val call: Call<Direction> =
                nearbyApi.getDirectionWithWayPoints(origin, destination, waypoints, key)
            call.enqueue(object : Callback<Direction> {
                override fun onResponse(
                    call: Call<Direction>,
                    response: Response<Direction>
                ) {
                    response.body()?.toString()?.let { Log.d("Res12345", it) }
                    //val lat = response.body()?.routes?.get(0)?.geometry?.location?.lat
                    //mMap?.clear()

                }

                override fun onFailure(call: Call<Direction>, t: Throwable) {
                    Log.e("Response111", "Failure")
                }
            })
        }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }


    override fun onMarkerDragEnd(p0: Marker?) {
        Log.d(TAG, "onMarkerDragStart: ")
        mMap!!.clear()
    }

    override fun onMarkerDragStart(p0: Marker?) {
        Log.d(TAG, "onMarkerDrag: ")
    }

    override fun onMarkerDrag(p0: Marker?) {
        Log.d(TAG, "onMarkerDragEnd: ")
//        mMap.setOnPolylineClickListener {  }
        val latLng = p0?.position
        try {
            val addresses = geocoder!!.getFromLocation(latLng!!.latitude, latLng.longitude, 1)
            if (addresses.size > 0) {
                val address = addresses[0]
                val streetAddress = address.getAddressLine(0)
                p0.title = streetAddress
            }
            Toast.makeText(
                this@MapsActivity,
                latLng.latitude.toString() + " " + latLng.longitude,
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onMapLongClick(latLng: LatLng?) {
        Log.d(TAG, "onMapLongClick: $latLng")
        mMap!!.clear()
        try {
            val addresses = geocoder!!.getFromLocation(latLng?.latitude!!, latLng?.longitude!!, 1)
            if (addresses.size > 0) {
                val address = addresses[0]
                val streetAddress = address.getAddressLine(0)
                mMap?.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(streetAddress)
                        .draggable(true)
                )
                Toast.makeText(
                    this@MapsActivity,
                    latLng.latitude.toString() + " " + latLng.longitude,
                    Toast.LENGTH_SHORT
                ).show()

                //place2 = MarkerOptions().position(LatLng(latLng.latitude, latLng.longitude))
                //.title("Location To Go")
                addressClick = addresses.toString()
                //mMap?.addMarker(place2)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        private const val TAG = "MapsActivity"

    }

    override fun onMarkerClick(p0: Marker?): Boolean {
//        var lat = p0?.position?.latitude
//        var lng= p0?.position?.longitude

        //var latPlace2 =lat
        //var lngPlace2 =lng

        return true
    }
}

private fun <T> Call<T>.enqueue(callback: Callback<NearByPlace>) {

}
