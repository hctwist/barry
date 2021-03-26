package uk.henrytwist.projectbarry.application.view.daily

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import uk.henrytwist.androidbasics.livedata.immutable
import uk.henrytwist.androidbasics.navigation.NavigatorViewModel
import uk.henrytwist.projectbarry.application.view.components.HeaderAdapter
import uk.henrytwist.projectbarry.domain.models.DailyForecast
import uk.henrytwist.projectbarry.domain.usecases.GetDailyForecast
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class DailyViewModel @Inject constructor(
        private val getDailyForecast: GetDailyForecast
) : NavigatorViewModel(), HeaderAdapter.Handler {

    private val _forecast = MutableLiveData<DailyForecast>()
    val forecast = _forecast.immutable()

    init {

        viewModelScope.launch {

            getDailyForecast().ifSuccessful {

                _forecast.value = it
            }
        }
    }

    override fun onClickBack() {

        navigateBack()
    }
}