package com.example.nearbyplace.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.nearbyplace.R
import com.example.nearbyplace.model.nearby.NearByPlace
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val nearByPlace = mutableListOf<NearByPlace>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnRestaurents.setOnClickListener {
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            intent.putExtra("keys", "restaurant")
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

        Glide.with(this).load("https://maps.googleapis.com/maps/api/place/photo?photoreference=CmRaAAAAd5d7SdYV_G1PWnuTWIhI4gfnvjhGoVlu229ld6Ss9_qGLouiaJpLF06ARhSrdmDjCggPwUDiYAuwe2_50eu6YABgGfrGi7-ktubccpH9gFk39F5Zma-MrLsrli_tudffEhDI5K6D8FrmRKvHkgJIaKQuGhTqOWtokckdCap8cNlLHvElsBeZlg&sensor=false&maxheight=2009&maxwidth=1340&key=AIzaSyDtxS6znDp9TzYPYdV8XwptR-ARnFHKRCs").into(imgtest);


    }
}


