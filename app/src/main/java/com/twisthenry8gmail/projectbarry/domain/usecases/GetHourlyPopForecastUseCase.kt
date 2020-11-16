package com.twisthenry8gmail.projectbarry.domain.usecases

import com.twisthenry8gmail.projectbarry.application.data.openweather.OneCallRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetHourlyPopForecastUseCase @Inject constructor(
    private val oneCallRepository: OneCallRepository
) {

//    suspend operator fun invoke(location: LocationData): Result<HourlyForecast2> {
//
//        val oneCallResult = oneCallRepository.get(location)
//
//        return oneCallResult.map {
//
//            buildPopForecast(it)
//        }
//    }
//
//    private fun buildPopForecast(oneCallData: OpenWeatherSource.OneCallData): HourlyForecast2 {
//
//        val hours = oneCallData.hourly.map {
//
//            HourlyForecast2.Hour(
//
//                Instant.ofEpochSecond(it.time).atZone(ZoneId.systemDefault()),
//                OpenWeatherCodeMapper.map(it.conditionCode),
//                ForecastElement.Pop(it.pop),
//                it.pop
//            )
//        }
//
//        return HourlyForecast2(hours, 0.0, 1.0)
//    }
}