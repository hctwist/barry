package uk.henrytwist.projectbarry.application.view.main

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import uk.henrytwist.androidbasics.livedata.immutable
import uk.henrytwist.androidbasics.livedata.trigger
import uk.henrytwist.androidbasics.navigation.NavigatorViewModel
import uk.henrytwist.kotlinbasics.Outcome
import uk.henrytwist.kotlinbasics.Trigger
import uk.henrytwist.kotlinbasics.successOrNull
import uk.henrytwist.kotlinbasics.waiting
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.domain.models.Location
import uk.henrytwist.projectbarry.domain.models.NowForecast
import uk.henrytwist.projectbarry.domain.models.SelectedLocation
import uk.henrytwist.projectbarry.domain.usecases.GetNowForecast
import uk.henrytwist.projectbarry.domain.usecases.GetSelectedLocation
import uk.henrytwist.projectbarry.domain.usecases.NeedsLocationPermission
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(
        private val getSelectedLocation: GetSelectedLocation,
        private val needsLocationPermission: NeedsLocationPermission,
        private val getNowForecast: GetNowForecast
) : NavigatorViewModel() {

    private val _locationPermissionQuery = MutableLiveData<Trigger>()
    val locationPermissionQuery: LiveData<Trigger>
        get() = _locationPermissionQuery

    private val _requestLocationPermission = MutableLiveData<Trigger>()
    val requestLocationPermission = _requestLocationPermission.immutable()

    private val _selectedLocation = MutableStateFlow<Outcome<SelectedLocation>>(waiting())
    val selectedLocation = _selectedLocation.map { it.successOrNull() }.asLiveData()

    private val _refreshing = MutableLiveData(false)
    val refreshing = _refreshing.immutable()

    private val _currentForecast = MutableStateFlow<Outcome<NowForecast>>(waiting())
    private val currentForecast = _currentForecast.asLiveData()
    private val successfulCurrentForecast = currentForecast.map { it.successOrNull() }

    val condition = successfulCurrentForecast.map { it?.condition }

    val conditionChange = successfulCurrentForecast.map { it?.conditionChange }

    val currentTemperature = successfulCurrentForecast.map { it?.temp }

    val feelsLikeTemperature = successfulCurrentForecast.map { it?.feelsLike }

    val elements = successfulCurrentForecast.map { it?.elements ?: listOf() }

    val daySnapshot = successfulCurrentForecast.map { it?.daySnapshot }

    val hourSnapshots = successfulCurrentForecast.map { it?.hourSnapshots ?: listOf() }

    val loadingStatus = MutableLiveData(LoadingStatus.LOADING_LOCATION)

    init {

        viewModelScope.launch {

            if (needsLocationPermission()) {

                _locationPermissionQuery.value = Trigger()
            } else {

                startFetching()
            }
        }
    }

    fun onLocationPermissionResult(granted: Boolean, shouldShowRationale: Boolean) {

        when {
            granted -> startFetching()
            shouldShowRationale -> navigate(R.id.action_fragmentMain2_to_fragmentLocationPermission)
            else -> _requestLocationPermission.trigger()
        }
    }

    private fun startFetching() {

        viewModelScope.launch {

            _selectedLocation.emitAll(getSelectedLocation())
        }

        viewModelScope.launch {

            _selectedLocation.collect { outcome ->

                outcome.successOrNull()?.location?.let {

                    fetchForecast(it)
                }
            }
        }
    }

    fun onRefresh() {

        viewModelScope.launch {

            _refreshing.value = true

            _selectedLocation.value.successOrNull()?.location?.let {

                fetchForecast(it)
            }

            _refreshing.value = false
        }
    }

    private suspend fun fetchForecast(location: Location) {

        loadingStatus.value = LoadingStatus.LOADING_FORECAST
        _currentForecast.value = getNowForecast.invoke(location)
        loadingStatus.value = LoadingStatus.LOADED
    }

    fun onLocationClicked() {

        navigate(R.id.action_fragmentMain_to_locationMenuFragment)
    }

    fun onMenuClick() {

        navigate(R.id.action_fragmentMain_to_mainMenuFragment)
    }

    fun onHourlyClicked() {

        navigate(R.id.action_fragmentMain_to_hourlyFragment)
    }

    fun onDailyClicked() {

        navigate(R.id.action_fragmentMain_to_dailyFragment)
    }

    enum class LoadingStatus {

        LOADING_LOCATION, LOADING_FORECAST, LOADED
    }
}