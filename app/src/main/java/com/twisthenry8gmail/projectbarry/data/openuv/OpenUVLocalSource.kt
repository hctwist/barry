package com.twisthenry8gmail.projectbarry.data.openuv

import android.content.SharedPreferences
import com.google.gson.Gson
import com.twisthenry8gmail.projectbarry.Result
import com.twisthenry8gmail.projectbarry.data.SharedPreferencesModule
import com.twisthenry8gmail.projectbarry.failure
import javax.inject.Inject

class OpenUVLocalSource @Inject constructor(@SharedPreferencesModule.Data val dataPreferences: SharedPreferences) :
    OpenUVSource {

    fun saveRealTimeUV(data: OpenUVSource.RealTimeData) {

        val gson = Gson()
        val json = gson.toJson(data)

        dataPreferences.edit().putString(REAL_TIME_CACHE, json).apply()
    }

    override suspend fun getRealTimeUV(
        lat: Double,
        lng: Double
    ): Result<OpenUVSource.RealTimeData> {

        val string = dataPreferences.getString(REAL_TIME_CACHE, null)

        return if (string == null) {

            failure()
        } else {

            val gson = Gson()
            val obj = gson.fromJson(string, OpenUVSource.RealTimeData::class.java)
            Result.Success(obj)
        }
    }

    companion object {

        private const val REAL_TIME_CACHE = "open_uv_real_time_cache"
    }
}