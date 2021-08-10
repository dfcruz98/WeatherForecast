package com.example.weatherforecast.ui.home

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.data.local.CityWeather
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: WeatherAdapter

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        requestLocationPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_home,
            container,
            false
        )

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getCitiesWeather()
            requestLocationPermission()
        }

        binding.currentLocationWeatherCard.setOnClickListener { view ->
            viewModel.currentLocationWeather.value?.let {
                showWeatherDetails(view, it)
            }
        }

        adapter = WeatherAdapter(object : WeatherAdapter.WeatherAdapterListener {
            override fun clickListener(view: View, cityWeather: CityWeather) {
                showWeatherDetails(view, cityWeather)
            }
        })

        binding.mainRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.mainRecyclerView.adapter = adapter

        return binding.root
    }

    private fun showWeatherDetails(view: View, cityWeather: CityWeather) {
        val transitionName = requireContext().getString(R.string.city_weather_transition)
        val extras = FragmentNavigatorExtras(view to transitionName)
        val action = HomeFragmentDirections.navigateToDetails(
            cityWeather.cityId,
            cityWeather.city,
            cityWeather.country
        )
        view.findNavController().navigate(action, extras)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.citiesWeather.observe(viewLifecycleOwner) { result ->
            result?.let {
                adapter.submitList(it)
            }
        }

        viewModel.currentLocationWeather.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                binding.weather = result
                binding.currentLocationWeatherCard.visibility = View.VISIBLE
            } else {
                binding.currentLocationWeatherCard.visibility = View.GONE
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
        }
    }


    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location == null) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.error_getting_location),
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                    return@addOnSuccessListener
                }

                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val address = addresses.first()

                viewModel.getCurrentLocationWeather("${address.locality},${address.countryCode}")
                return@addOnSuccessListener
            }
    }

    private fun requestLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                getCurrentLocation()
            }
            shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION) -> {
                Toast.makeText(
                    requireContext().applicationContext,
                    getString(R.string.show_error_permission_location_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {
                requestPermissionLauncher.launch(
                    ACCESS_FINE_LOCATION
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getCurrentLocation()
        } else {
            Snackbar.make(
                binding.root,
                getString(R.string.show_error_permission_location_denied),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

}