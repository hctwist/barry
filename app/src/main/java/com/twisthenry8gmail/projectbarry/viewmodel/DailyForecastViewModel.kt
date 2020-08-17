package com.twisthenry8gmail.projectbarry.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.twisthenry8gmail.projectbarry.data.DailyForecast
import com.twisthenry8gmail.projectbarry.Result
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocationRepository
import com.twisthenry8gmail.projectbarry.successOrNull
import com.twisthenry8gmail.projectbarry.usecases.DailyForecastUseCase
import com.twisthenry8gmail.projectbarry.viewmodel.navigator.NavigatorViewModel
import javax.inject.Inject

class DailyForecastViewModel @Inject constructor(
    private val dailyForecastUseCase: DailyForecastUseCase,
    val locationRepository: ForecastLocationRepository
) : NavigatorViewModel() {

    private val _forecasts = MutableLiveData<Result<List<DailyForecast>>>()

    val successfulForecasts = _forecasts.map { it.successOrNull() ?: listOf() }

    init {

//        viewModelScope.launch {
//
//            val selectedLocation = locationRepository.getSelectedLocation()
//
//            if (selectedLocation is Result.Success) {
//
//                _forecasts.value =
//                    dailyForecastUseCase.getDailyForecasts(selectedLocation.data, false)
//            }
//        }
    }
}