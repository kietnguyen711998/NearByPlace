package com.example.nearbyplace

import com.example.nearbyplace.model.directions.Direction
import com.example.nearbyplace.model.directions.DirectionResults
import com.example.nearbyplace.model.nearby.NearByPlace
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NearbyApi {
    @GET("place/nearbysearch/json?")
    fun getDetails(
        @Query("location") loc: String?,
        @Query("radius") radius: String?,
        @Query("types") type: String,
        @Query("key") key: String?
    ): Call<NearByPlace>

    @GET("directions/json")
    fun getDirectionWithWayPoints(
        @Query("origin") origin: String?,
        @Query("destination") destination: String?,
        @Query("waypoints") wayPoints: String?,
        @Query("key") key: String?
    ): Call<Direction>
}
