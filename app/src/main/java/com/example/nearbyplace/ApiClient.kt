package com.example.nearbyplace

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    companion object {
        var retrofit: Retrofit? = null
        val baseurl = "https://maps.googleapis.com/maps/api/"
        fun getClient(): Retrofit? {
            if (retrofit == null) {
                retrofit = Retrofit.Builder().baseUrl(baseurl)
                    .addConverterFactory(GsonConverterFactory.create()).build()
            }
            return retrofit
        }
    }

}