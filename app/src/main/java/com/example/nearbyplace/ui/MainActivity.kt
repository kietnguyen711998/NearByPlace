package com.example.nearbyplace.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nearbyplace.R
import com.example.nearbyplace.model.nearby.NearByPlace
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var loc = "16.068264,108.214405"
    var radius = "10000"
    var type = "school"
    var key = "AIzaSyDtxS6znDp9TzYPYdV8XwptR-ARnFHKRCs"

    private val nearByPlace = mutableListOf<NearByPlace>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnRestaurents.setOnClickListener {
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            //intent.putExtra("keys", "restaurent")
            startActivity(intent);
        }

        btnHopital.setOnClickListener {
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            intent.putExtra("keys", "hospital")
            startActivity(intent);
        }

        btnHotel.setOnClickListener {
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            intent.putExtra("keys", "hotel")
            startActivity(intent);
        }

        btnSchool.setOnClickListener {
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            intent.putExtra("keys", "school")
            startActivity(intent);
        }

    }
}


