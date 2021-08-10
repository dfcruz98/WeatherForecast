package com.example.weatherforecast.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherforecast.data.local.CityWeather
import com.example.weatherforecast.data.local.WeatherDao
import com.example.weatherforecast.data.local.WeatherDatabase
import com.example.weatherforecast.data.local.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class WeatherDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WeatherDatabase
    private lateinit var dao: WeatherDao

    @Before
    fun setup() {
        database = Room
            .inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(), WeatherDatabase::class.java
            )
            .allowMainThreadQueries()
            .build()

        dao = database.weatherDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insert() = runBlockingTest {
        val weather = CityWeather(
            1, "Lisbon", "PT", "01",
            25.2, "", 2, 2, "999"
        )

        dao.insertCityWeather(weather)

        val citiesWeather = dao.getCitiesWeather().getOrAwaitValue()

        assertThat(citiesWeather).contains(weather)
    }

    @Test
    fun `get cities weather`() = runBlockingTest {
        val weatherLisbon = CityWeather(
            1, "Lisbon", "PT", "01",
            25.2, "", 2, 2, "999"
        )

        dao.insertCityWeather(weatherLisbon)

        val weatherLondon = CityWeather(
            2, "London", "PT", "01",
            25.2, "", 2, 2, "999"
        )

        dao.insertCityWeather(weatherLondon)

        val citiesWeather = dao.getCitiesWeather().getOrAwaitValue()

        assertThat(citiesWeather.size).isEqualTo(2)
    }

    @Test
    fun `test insert same id conflict`() = runBlockingTest {
        val weather = CityWeather(
            1, "Lisbon", "PT", "01",
            25.2, "", 2, 2, "999"
        )

        dao.insertCityWeather(weather)

        val weatherUpdated = CityWeather(
            1, "Lisbon", "PT", "01",
            26.2, "", 2, 2, "999"
        )

        dao.insertCityWeather(weatherUpdated)

        val citiesWeather = dao.getCityWeather(weather.cityId).getOrAwaitValue()

        assertThat(citiesWeather?.temperature).isEqualTo(weatherUpdated.temperature)
    }


}