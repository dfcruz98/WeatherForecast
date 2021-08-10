package com.example.weatherforecast.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherforecast.data.local.getOrAwaitValue
import com.example.weatherforecast.data.FakeWeatherRepository
import com.example.weatherforecast.ui.home.HomeViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class HomeViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        viewModel = HomeViewModel(FakeWeatherRepository())
    }

    @Test
    fun `get cities weather`() = runBlockingTest {
        viewModel.getCitiesWeather()
        val citiesWeather = viewModel.citiesWeather.getOrAwaitValue()
        assertThat(citiesWeather).isNotEmpty()
    }
}