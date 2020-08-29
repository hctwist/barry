package com.twisthenry8gmail.projectbarry.data.openweather

import android.content.SharedPreferences
import com.google.gson.Gson
import com.twisthenry8gmail.projectbarry.Result
import com.twisthenry8gmail.projectbarry.data.SharedPreferencesModule
import com.twisthenry8gmail.projectbarry.failure
import javax.inject.Inject

class OpenWeatherLocalSource @Inject constructor(@SharedPreferencesModule.Data val dataPreferences: SharedPreferences) :
    OpenWeatherSource {

    // TODO Cache multiple calls for different locations
    // TODO Gson singleton
    // TODO Background thread
    fun saveOneCallData(data: OpenWeatherSource.OneCallData) {

        val gson = Gson()
        val json = gson.toJson(data)

        dataPreferences.edit().putString(ONE_CALL_CACHE, json).apply()
    }

    override suspend fun getOneCallData(
        lat: Double,
        lng: Double
    ): Result<OpenWeatherSource.OneCallData> {

        val string = dataPreferences.getString(ONE_CALL_CACHE, null)

        return if (string == null) {

            failure()
        } else {

            val gson = Gson()
            val obj = gson.fromJson(string, OpenWeatherSource.OneCallData::class.java)
            Result.Success(obj)
        }
    }

    companion object {

        private const val ONE_CALL_CACHE = "open_weather_one_call_cache"
    }
}