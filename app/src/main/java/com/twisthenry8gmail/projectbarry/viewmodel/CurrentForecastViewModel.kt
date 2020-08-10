package com.twisthenry8gmail.projectbarry.viewmodel

import androidx.lifecycle.*
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.Trigger
import com.twisthenry8gmail.projectbarry.data.*
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocation
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocationRepository
import com.twisthenry8gmail.projectbarry.usecases.CurrentForecastUseCase
import com.twisthenry8gmail.projectbarry.view.WeatherConditionDisplay
import com.twisthenry8gmail.projectbarry.view.currentforecast.Feature
import com.twisthenry8gmail.projectbarry.view.locations.LocationUtil
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class CurrentForecastViewModel @Inject constructor(
    private val forecastLocationRepository: ForecastLocationRepository,
    private val currentForecastUseCase: CurrentForecastUseCase,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val temperatureScale = settingsRepository.getTemperatureScale()

    private val _location = MutableLiveData<Result<ForecastLocation>>()
    val locationIconRes = _location.map { result ->

        result.successOrNull()?.type?.let { LocationUtil.resolveIconRes(it) }
            ?: R.drawable.round_location_disabled_24
    }
    val locationName = _location.map { result ->

        result.successOrNull()?.name
    }

    private val _showLocationMenu = MutableLiveData<Trigger>()
    val showLocationMenu: LiveData<Trigger>
        get() = _showLocationMenu

    private val refreshInterval = settingsRepository.getRefreshInterval()

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean>
        get() = _refreshing

    private val currentForecast = MutableLiveData<Result<CurrentForecast>>()
    val successfulCurrentForecast = currentForecast.map { it.successOrNull() }
    val conditionIcon = successfulCurrentForecast.map {

        it?.let { WeatherConditionDisplay.getImageResource(it.condition, true) }
    }
    val currentTemperature = currentForecast.map { it.successOrNull()?.temp?.to(temperatureScale) }
    val features = currentForecast.map {

        if (it is Result.Success) {

            val currentTime = ZonedDateTime.now()
            val sunriseSunsetFeature = if (it.data.sunrise.isAfter(currentTime)) {

                Feature.Sunrise(it.data.sunrise)
            } else {

                Feature.Sunset(it.data.sunset)
            }

            listOf(
                Feature.TemperatureLow(it.data.tempLow.to(temperatureScale)),
                Feature.TemperatureHigh(it.data.tempHigh.to(temperatureScale)),
                Feature.UVIndex(it.data.uvIndex),
                Feature.Pop(it.data.hourly[0].pop),
                Feature.FeelsLike(it.data.feelsLike.to(temperatureScale)),
                sunriseSunsetFeature
            )
        } else {

            listOf(
                Feature.TemperatureLow(null),
                Feature.TemperatureHigh(null),
                Feature.UVIndex(null),
                Feature.Pop(null),
                Feature.FeelsLike(null),
                Feature.Sunset(null)
            )
        }
    }

    val hourly = successfulCurrentForecast.map { current ->

        if (current == null) {

            listOf()
        } else {

            val now = ZonedDateTime.now()
            val upperBound = now.plusDays(1).withHour(4)
            current.hourly.filter {

                it.time.isBefore(upperBound) && it.time.isAfter(now)
            }.map {

                it.copy(temp = it.temp.to(temperatureScale))
            }
        }
    }

    private val selectedPlaceListener: (String?) -> Unit = {

        viewModelScope.launch {

            if (_location.value?.successOrNull()?.placeId != it) {

                _location.value = forecastLocationRepository.getSelectedLocation()
                fetchCurrentForecast(true)
            }
        }
    }

    init {

        forecastLocationRepository.registerSelectedPlaceListener(selectedPlaceListener)

        viewModelScope.launch {

            _location.value = forecastLocationRepository.getSelectedLocation()
            fetchCurrentForecast(false)
        }
    }

    // TODO Stop multiple refreshes in short time period
    fun forceRefresh() {

        viewModelScope.launch {

            _refreshing.value = true
            fetchCurrentForecast(true)
            _refreshing.value = false
        }
    }

    fun onLocationClicked() {

        _showLocationMenu.value = Trigger()
    }

    private suspend fun fetchCurrentForecast(forceRefresh: Boolean) {

        _location.value?.successOrNull()?.let { l ->

            val forecast = currentForecastUseCase.getCurrentForecast(l, forceRefresh)

            if (!forceRefresh && forecast is Result.Success && ChronoUnit.SECONDS.between(
                    forecast.data.timestamp,
                    Instant.now()
                ) > refreshInterval
            ) {

                currentForecast.value = currentForecastUseCase.getCurrentForecast(l, true)
            } else {

                currentForecast.value = forecast
            }
        }
    }

    override fun onCleared() {

        forecastLocationRepository.deregisterSelectedPlaceListener(selectedPlaceListener)
    }
}