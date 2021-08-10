package com.example.weatherforecast.di

import android.content.Context
import androidx.room.Room
import com.example.weatherforecast.BuildConfig
import com.example.weatherforecast.data.local.WeatherDao
import com.example.weatherforecast.data.local.WeatherDatabase
import com.example.weatherforecast.repository.WeatherRepository
import com.example.weatherforecast.repository.WeatherRepositoryImpl
import com.example.weatherforecast.data.remote.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideWeatherDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            WeatherDatabase::class.java,
            "weather_database"
        ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideWeatherDao(database: WeatherDatabase) = database.weatherDao()


    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): WeatherService =
        retrofit.create(WeatherService::class.java)

    @Singleton
    @Provides
    fun providePhotosRepository(api: WeatherService, db: WeatherDao): WeatherRepository =
        WeatherRepositoryImpl(api, db)


    @Singleton
    @Provides
    fun provideInterceptor(): OkHttpClient {
        val builder = OkHttpClient
            .Builder()

        // Logs in the terminal the request/response made
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC

        builder.addInterceptor(logging)

        // Add new interceptor that is going to add new parameters to every request
        builder.addInterceptor(Interceptor { chain ->
            val original = chain.request()
            val originalUrl = original.url

            val url = originalUrl.newBuilder()
                .addQueryParameter("appid", BuildConfig.API_KEY)
                .build()

            val requestBuilder = original.newBuilder().url(url)

            chain.proceed(requestBuilder.build())
        })

        return builder.build()
    }

}