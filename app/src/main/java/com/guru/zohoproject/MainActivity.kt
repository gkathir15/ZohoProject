package com.guru.zohoproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator

class MainActivity : AppCompatActivity(), ClickListener {
    // private lateinit var binding: ActivityMainBinding
    private lateinit var countryList: RecyclerView
    private lateinit var loading: CircularProgressIndicator
    private lateinit var noData: TextView

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var countryAdapter: CountryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // binding = ActivityMainBinding.inflate(layoutInflater)
        countryList = findViewById(R.id.countryList)
        loading = findViewById(R.id.loader)
        noData = findViewById(R.id.noData)

        findViewById<SearchView>(R.id.searchView).setOnCloseListener {
            viewModel.seachCleared()
            true
        }
        findViewById<SearchView>(R.id.searchView).setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.performSearch(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.performSearch(it) }
                return false
            }
        })
        countryAdapter = CountryAdapter(this)
        countryList.also {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = countryAdapter
        }


        viewModel.countryList.observe(this, {
            countryList.visibility = View.VISIBLE
            loading.visibility = View.GONE
            noData.visibility = View.GONE
            countryAdapter.list = it
            countryAdapter.notifyDataSetChanged()
        })
        viewModel.isLoading.observe(this, {
            if (it) {
                countryList.visibility = View.GONE
                loading.also {
                    it.visibility = View.VISIBLE
                    it.isIndeterminate = true
                }
                noData.visibility = View.GONE
            }else{
                loading.visibility = View.GONE
            }
        })
        viewModel.errorMessage.observe(this, {
            countryList.visibility = View.GONE
            loading.visibility = View.GONE
            noData.also { it1->
                it1.visibility = View.VISIBLE
                it1.text = it
            }
        })

        viewModel.getAllCountries()


    }

    override fun onCountryClicked(country: Country.CountryItem) {
        startActivity(Intent(this, Details::class.java).also {
            it.putExtra("country", country)
        })
    }
}