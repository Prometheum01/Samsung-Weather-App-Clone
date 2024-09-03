package com.rurouni.weatherapp.di

import android.content.Context
import androidx.room.Room
import com.rurouni.weatherapp.data.source.local.db.WeatherDatabase
import com.rurouni.weatherapp.data.source.local.db.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDb(@ApplicationContext appContext: Context) : WeatherDatabase {
        return Room.databaseBuilder(appContext, WeatherDatabase::class.java, "weather.db").build()
    }

    @Provides
    fun provideWeatherDao(db: WeatherDatabase) : WeatherDao {
        return db.weatherDao()
    }
}