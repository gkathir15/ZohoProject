package com.guru.zohoproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager


/**Created by Guru kathir.J on 27,October,2021 **/
class CountryAdapter(val listener:ClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var list: List<Region> = emptyList<Region>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            HeaderViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.region_header_item, parent, false)
            )
        } else {
            CountryViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.flag_item, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.header.text = list[position].regionName
            holder.header.isEnabled = false
        } else if (holder is CountryViewHolder) {
            holder.countryName.text = list[position].country?.name
            holder.flagV.loadImage(Glide.with(holder.flagV), list[position].country?.flags?.png)
            holder.scaff.setOnClickListener { list[position].country?.let { it1 ->
                listener.onCountryClicked(
                    it1
                )
            } }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].isCountry)
            1
        else 0
    }

    class HeaderViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val header = v.findViewById<TextView>(R.id.regionTv)
    }

    class CountryViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val countryName = v.findViewById<TextView>(R.id.countryNameTv)
        val flagV = v.findViewById<ImageView>(R.id.flagIv)
        val scaff = v.findViewById<RelativeLayout>(R.id.scaffold)
    }
}

fun ImageView.loadImage(glide: RequestManager, url: String?) {
    glide.load(url).into(this)
}