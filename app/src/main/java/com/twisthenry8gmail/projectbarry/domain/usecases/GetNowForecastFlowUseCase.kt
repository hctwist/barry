package com.twisthenry8gmail.projectbarry.domain.usecases

import com.twisthenry8gmail.projectbarry.application.data.openuv.OpenUVSource
import com.twisthenry8gmail.projectbarry.application.data.openuv.RealTimeUVRepository
import com.twisthenry8gmail.projectbarry.application.data.openweather.OneCallRepository
import com.twisthenry8gmail.projectbarry.application.data.openweather.OpenWeatherCodeMapper
import com.twisthenry8gmail.projectbarry.application.data.openweather.OpenWeatherSource
import com.twisthenry8gmail.projectbarry.domain.SettingsRepository
import com.twisthenry8gmail.projectbarry.domain.core.*
import com.twisthenry8gmail.projectbarry.domain.data.locations.SelectedLocationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.abs

@ExperimentalCoroutinesApi
class GetNowForecastFlowUseCase @Inject constructor(
    private val selectedLocationRepository: SelectedLocationRepository,
    private val oneCallRepository: OneCallRepository,
    private val realTimeUVRepository: RealTimeUVRepository,
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(): Flow<Result<CurrentForecast>> {

        return selectedLocationRepository.selectedLocation.map { locationResult ->

            locationResult.switchMap { location ->

                val locationData = location.locationData

                if (locationData == null) {

                    waiting()
                } else {

                    coroutineScope {

                        val oneCallDataJob = async { oneCallRepository.get(locationData) }
                        val realTimeUVDataJob = async { realTimeUVRepository.get(locationData) }

                        val oneCallData = oneCallDataJob.await()
                        val realTimeUVData = realTimeUVDataJob.await()

                        if (oneCallData is Result.Success && realTimeUVData is Result.Success) {

                            success(buildCurrentForecast(oneCallData.data, realTimeUVData.data))
                        } else {

                            failure()
                        }
                    }
                }
            }
        }
    }

    private fun buildCurrentForecast(
        oneCall: OpenWeatherSource.OneCallData,
        uv: OpenUVSource.RealTimeData
    ): CurrentForecast {

        val temperatureScale = settingsRepository.getTemperatureScale()

        val condition = OpenWeatherCodeMapper.map(oneCall.conditionCode)

        val today = oneCall.daily[0]

        val nHours = 2
        var totalPop = 0.0
        for (i in 0 until nHours) {

            val hour = oneCall.hourly[i]
            totalPop += hour.pop
        }
        val pop = totalPop / nHours

        val nowInstant = Instant.now()

        var sunriseSunsetElement: ForecastElement? = null
        for (i in oneCall.daily.indices) {

            val day = oneCall.daily[i]

            val sunriseInstant = Instant.ofEpochSecond(day.sunrise)
            if (sunriseInstant.isAfter(nowInstant)) {

                sunriseSunsetElement = ForecastElement.Sunrise(
                    ZonedDateTime.ofInstant(
                        sunriseInstant,
                        ZoneOffset.UTC
                    )
                )
                break
            } else {

                val sunsetInstant = Instant.ofEpochSecond(day.sunset)
                if (sunsetInstant.isAfter(nowInstant)) {

                    sunriseSunsetElement = ForecastElement.Sunset(
                        ZonedDateTime.ofInstant(
                            sunsetInstant,
                            ZoneOffset.UTC
                        )
                    )
                    break
                }
            }
        }

        // TODO Elements from settings
        val elements = listOf(

            ForecastElement.UVIndex(uv.uv),
            ForecastElement.Pop(pop),
            sunriseSunsetElement!!
        )

        val nHourSnapshots = 4
        val hourSnapshots = ArrayList<HourSnapshot>(nHourSnapshots)

        val nextHour = if (abs(
                Instant.ofEpochSecond(oneCall.hourly[0].time)
                    .until(nowInstant, ChronoUnit.MINUTES)
            ) > 40 || OpenWeatherCodeMapper.map(oneCall.hourly[0].conditionCode).group == condition.group
        ) {

            oneCall.hourly[1]
        } else {

            oneCall.hourly[0]
        }
        val nextHourCondition = OpenWeatherCodeMapper.map(nextHour.conditionCode)
        hourSnapshots.add(
            nextHour.toSnapshot(
                nextHourCondition,
                condition.group != nextHourCondition.group
            )
        )

        var currentHourSnapshots = 1
        var lastHourSnapshotCondition = nextHourCondition
        var hourGap = 0
        for (i in 1 until oneCall.hourly.size) {

            hourGap++

            val hour = oneCall.hourly[i]
            val hourCondition = OpenWeatherCodeMapper.map(hour.conditionCode)

            if (hourCondition != lastHourSnapshotCondition) {

                hourSnapshots.add(hour.toSnapshot(hourCondition, true))
                currentHourSnapshots++
                lastHourSnapshotCondition = hourCondition
                hourGap = 0
            } else if (hourGap == 3) {

                hourSnapshots.add(hour.toSnapshot(hourCondition, false))
                currentHourSnapshots++
                lastHourSnapshotCondition = hourCondition
                hourGap = 0
            }

            if (currentHourSnapshots == nHourSnapshots) break
        }

        val daySnapshots = oneCall.daily.subList(0, 3).map {

            DaySnapshot(
                Instant.ofEpochSecond(it.time).atZone(ZoneId.systemDefault()),
                ScaledTemperature.fromKelvin(it.tempLow).to(temperatureScale),
                ScaledTemperature.fromKelvin(it.tempHigh).to(temperatureScale),
                OpenWeatherCodeMapper.map(it.conditionCode),
                it.pop
            )
        }

        return CurrentForecast(
            ScaledTemperature.fromKelvin(
                oneCall.temp
            ).to(temperatureScale),
            ScaledTemperature.fromKelvin(
                today.tempLow
            ).to(temperatureScale),
            ScaledTemperature.fromKelvin(
                today.tempHigh
            ).to(temperatureScale),
            condition,
            elements,
            hourSnapshots,
            daySnapshots
        )
    }

    private fun OpenWeatherSource.OneCallData.Hour.toSnapshot(
        condition: WeatherCondition,
        important: Boolean
    ): HourSnapshot {

        return HourSnapshot(
            Instant.ofEpochSecond(time).atZone(ZoneId.systemDefault()),
            condition,
            important
        )
    }
}