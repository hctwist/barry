package uk.henrytwist.projectbarry.application.view.hourly

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import uk.henrytwist.androidbasics.livedata.immutable
import uk.henrytwist.androidbasics.navigation.NavigatorViewModel
import uk.henrytwist.projectbarry.application.view.components.HeaderAdapter
import uk.henrytwist.projectbarry.domain.models.HourlyForecast
import uk.henrytwist.projectbarry.domain.usecases.GetHourlyPopForecast
import uk.henrytwist.projectbarry.domain.usecases.GetHourlyTemperatureForecast
import uk.henrytwist.projectbarry.domain.usecases.GetHourlyUVIndexForecast
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class HourlyViewModel @Inject constructor(
        private val getHourlyPopForecast: GetHourlyPopForecast,
        private val getHourlyTemperatureForecast: GetHourlyTemperatureForecast,
        private val getHourlyUVIndexForecast: GetHourlyUVIndexForecast
) : NavigatorViewModel(), HeaderAdapter.Handler, HourlyHeaderAdapter.Handler {

    private var currentType = HourlyElementType.TEMPERATURE

    private val _forecast = MutableLiveData<HourlyForecast<*>>()
    val forecast = _forecast.immutable()

    init {

        viewModelScope.launch {

            fetchPendingForecast()
        }
    }

    override fun onClickBack() {

        navigateBack()
    }

    override fun onTypeChanged(type: HourlyElementType) {

        if (type == currentType) return

        currentType = type
        fetchPendingForecast()
    }

    private fun fetchPendingForecast() {

        viewModelScope.launch {

            val outcome = when (currentType) {

                HourlyElementType.POP -> getHourlyPopForecast()

                HourlyElementType.TEMPERATURE -> getHourlyTemperatureForecast()

                HourlyElementType.UV -> getHourlyUVIndexForecast()

                HourlyElementType.WIND_SPEED -> TODO()
            }

            outcome.ifSuccessful {

                _forecast.value = it
            }
        }
    }
}