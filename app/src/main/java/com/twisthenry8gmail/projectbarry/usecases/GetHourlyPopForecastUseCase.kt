package com.twisthenry8gmail.projectbarry.usecases

import com.twisthenry8gmail.projectbarry.core.Result
import com.twisthenry8gmail.projectbarry.core.HourlyForecast
import com.twisthenry8gmail.projectbarry.core.ForecastLocation
import com.twisthenry8gmail.projectbarry.data.openweather.OneCallRepository
import com.twisthenry8gmail.projectbarry.data.openweather.OpenWeatherCodeMapper
import com.twisthenry8gmail.projectbarry.data.openweather.OpenWeatherSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetHourlyPopForecastUseCase @Inject constructor(
    private val oneCallRepository: OneCallRepository
) {

    suspend operator fun invoke(location: ForecastLocation): Result<List<HourlyForecast.Pop>> {

        val oneCallResult = oneCallRepository.get(location)

        return oneCallResult.map {

            buildPopForecast(it)
        }
    }

    private fun buildPopForecast(oneCallData: OpenWeatherSource.OneCallData): List<HourlyForecast.Pop> {

        return oneCallData.hourly.map {

            HourlyForecast.Pop(

                Instant.ofEpochSecond(it.time).atZone(ZoneId.systemDefault()),
                OpenWeatherCodeMapper.map(it.conditionCode),
                it.pop
            )
        }
    }
}