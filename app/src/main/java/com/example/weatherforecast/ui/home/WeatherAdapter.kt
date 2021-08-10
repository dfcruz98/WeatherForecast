package com.example.weatherforecast.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.data.local.CityWeather
import com.example.weatherforecast.data.local.CityWeatherDiff
import com.example.weatherforecast.databinding.CityWeatherLayoutBinding

class WeatherAdapter(private val listener: WeatherAdapterListener) :
    ListAdapter<CityWeather, WeatherAdapter.WeatherViewHolder>(CityWeatherDiff) {

    interface WeatherAdapterListener {
        fun clickListener(view: View, cityWeather: CityWeather)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(
            listener,
            CityWeatherLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class WeatherViewHolder(
        private val listener: WeatherAdapterListener,
        private val binding: CityWeatherLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cityWeather: CityWeather) {
            binding.run {
                this.weather = cityWeather
                executePendingBindings()
                this.root.setOnClickListener {
                    listener.clickListener(it, cityWeather)
                }
            }
        }
    }

}

