package uk.henrytwist.projectbarry.application.view.hourly

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.henrytwist.androidbasics.livedata.immutable
import uk.henrytwist.androidbasics.navigation.NavigatorViewModel
import uk.henrytwist.projectbarry.application.view.components.HeaderAdapter
import uk.henrytwist.projectbarry.domain.models.HourlyForecast
import uk.henrytwist.projectbarry.domain.usecases.GetHourlyForecast
import javax.inject.Inject

@HiltViewModel
class HourlyViewModel @Inject constructor(
        private val getHourlyForecast: GetHourlyForecast
) : NavigatorViewModel(), HeaderAdapter.Handler, HourlyHeaderAdapter.Handler {

    private val _forecastType = MutableLiveData(HourlyForecastType.FORECAST)
    val forecastType = _forecastType.immutable()

    private val _forecast = MutableLiveData<HourlyForecast>()
    val forecast = _forecast.immutable()

    init {

        viewModelScope.launch {

            fetchForecast(forecastType.value!!)
        }
    }

    override fun onClickBack() {

        navigateBack()
    }

    override fun onTypeChanged(type: HourlyForecastType) {

        if (type == forecastType.value) return

        _forecastType.value = type
        viewModelScope.launch {

            fetchForecast(type)
        }
    }

    private suspend fun fetchForecast(type: HourlyForecastType) {

        getHourlyForecast(type).ifSuccessful {

            _forecast.value = it
        }
    }
}