package com.guru.zohoproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class Details : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val country = intent.getParcelableExtra<Country.CountryItem>("country") as Country.CountryItem

        Glide.with(this).load(country.flags?.png).into(findViewById<ImageView>(R.id.flagV))
        findViewById<TextView>(R.id.Name).text = (" Name: ${country.name}")
        findViewById<TextView>(R.id.region).text = (" Region: ${country.region}")
        findViewById<TextView>(R.id.callCode).text = (" Calling Code: ${country.callingCodes?.get(0)}")

    }




}