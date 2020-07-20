package com.example.nearbyplace.model.directions


import com.google.gson.annotations.SerializedName

data class Direction(
    @SerializedName("geocoded_waypoints")
    var geocodedWaypoints: List<GeocodedWaypoint>,
    @SerializedName("routes")
    var routes: List<Route>,
    @SerializedName("status")
    var status: String
)