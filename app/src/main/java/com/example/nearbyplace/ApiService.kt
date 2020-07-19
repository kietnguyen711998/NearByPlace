package com.example.nearbyplace

import com.example.nearbyplace.model.NearByPlace
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NearbyApi {
    @GET("json?")
    fun getDetails(
        @Query("location") loc: String?,
        @Query("radius") radius: String?,
        @Query("types") type: String?,
        @Query("key") key: String?
    ): Call<NearByPlace?>?
}
