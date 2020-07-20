package com.example.nearbyplace.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.nearbyplace.ApiClient
import com.example.nearbyplace.NearbyApi
import com.example.nearbyplace.R
import com.example.nearbyplace.model.NearByPlace
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    var loc = "16.068264,108.214405"
    var radius = "10000"
    var type = "school"
    var key = "AIzaSyDtxS6znDp9TzYPYdV8XwptR-ARnFHKRCs"

    private val nearByPlace = mutableListOf<NearByPlace>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_move.setOnClickListener {
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            startActivity(intent);
        }

        val nearbyApi: NearbyApi = ApiClient.getClient()!!.create(NearbyApi::class.java)
        val call: Call<NearByPlace> =
            nearbyApi.getDetails(loc, radius, type, key)
        call.enqueue(object : Callback<NearByPlace> {
            override fun onResponse(
                call: Call<NearByPlace>,
                response: Response<NearByPlace>
            ) {
                Log.d("Res", response.body()?.results?.get(0)?.name?: "")
                Log.d("Res", response.body()?.results?.get(0)?.id?: "")
                Log.d("Res", response.body()?.results?.get(0)?.vicinity?: "")

            }

            override fun onFailure(
                call: Call<NearByPlace>,
                t: Throwable
            ) {
                Log.e("Response", "Failure")
            }
        })
        
    }
}

private fun <T> Call<T>?.enqueue(callback: Callback<NearByPlace>) {

}

