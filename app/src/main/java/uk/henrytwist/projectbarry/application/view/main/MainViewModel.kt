package uk.henrytwist.projectbarry.application.view.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.google.android.gms.common.api.ResolvableApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import uk.henrytwist.androidbasics.livedata.event
import uk.henrytwist.androidbasics.livedata.immutable
import uk.henrytwist.androidbasics.livedata.trigger
import uk.henrytwist.androidbasics.navigation.NavigationCommand
import uk.henrytwist.androidbasics.navigation.NavigatorViewModel
import uk.henrytwist.kotlinbasics.Event
import uk.henrytwist.kotlinbasics.Trigger
import uk.henrytwist.kotlinbasics.outcomes.*
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.domain.data.LocationFailure
import uk.henrytwist.projectbarry.domain.models.Location
import uk.henrytwist.projectbarry.domain.models.NowForecast
import uk.henrytwist.projectbarry.domain.models.SelectedLocation
import uk.henrytwist.projectbarry.domain.usecases.GetNowForecast
import uk.henrytwist.projectbarry.domain.usecases.GetSelectedLocation
import uk.henrytwist.projectbarry.domain.usecases.GetSelectedLocationInvalidationTracker
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
        getSelectedLocationInvalidationTracker: GetSelectedLocationInvalidationTracker,
        private val getSelectedLocation: GetSelectedLocation,
        private val getNowForecast: GetNowForecast
) : NavigatorViewModel() {

    private val _queryLocationPermission = MutableLiveData<Trigger>()
    val queryLocationPermission = _queryLocationPermission.immutable()

    private val _requestLocationPermission = MutableLiveData<Trigger>()
    val requestLocationPermission = _requestLocationPermission.immutable()

    private val _queryLocationServices = MutableLiveData<Trigger>()
    val queryLocationServices = _queryLocationServices.immutable()

    private val _requestLocationServices = MutableLiveData<Event<ResolvableApiException>>()
    val requestLocationServices = _requestLocationServices.immutable()
    private var locationServicesRequested = false
    private var locationServicesAPIException: ResolvableApiException? = null

    private val selectedLocationInvalidationTracker = getSelectedLocationInvalidationTracker()
    private val _selectedLocation = MutableStateFlow<Outcome<SelectedLocation>>(waiting())
    val selectedLocation = _selectedLocation.map { it.successOrNull() }.asLiveData()

    private val _refreshing = MutableLiveData(false)
    val refreshing = _refreshing.immutable()

    private val _currentForecast = MutableStateFlow<Outcome<NowForecast>>(waiting())
    private val currentForecast = _currentForecast.asLiveData()
    private val successfulCurrentForecast = currentForecast.map { it.successOrNull() }

    val condition = successfulCurrentForecast.map { it?.condition }

    val conditionChange = successfulCurrentForecast.map { it?.conditionChange }

    val isNight = successfulCurrentForecast.map { it?.isNight ?: false }

    val currentTemperature = successfulCurrentForecast.map { it?.temp }

    val feelsLikeTemperature = successfulCurrentForecast.map { it?.feelsLike }

    val elements = successfulCurrentForecast.map { it?.elements ?: listOf() }

    val daySnapshot = successfulCurrentForecast.map { it?.daySnapshot }

    val hourSnapshots = successfulCurrentForecast.map { it?.hourSnapshots ?: listOf() }

    private val _status = MutableLiveData(Status.LOADING)
    val status = _status.distinctUntilChanged()

    private val _loadingStatus = MutableLiveData(LoadingStatus.LOADING_LOCATION)
    val loadingStatus = _loadingStatus.immutable()
    private var permissionStatus = PermissionStatus.RATIONALE

    init {

        viewModelScope.launch {

            selectedLocationInvalidationTracker.collect {

                if (it == null) {

                    _selectedLocation.value = SelectedLocation(null, true).asSuccess()
                    _queryLocationPermission.trigger()
                } else {

                    _selectedLocation.value = SelectedLocation(null, false).asSuccess()
                    collectLocation()
                }
            }
        }

        viewModelScope.launch {

            _selectedLocation.collect { outcome ->

                // TODO Compiler bug?
                when (outcome) {

                    is Outcome.Waiting -> {
                    }

                    is Outcome.Success -> {

                        outcome.successOrNull()?.location?.let {

                            fetchForecast(it)
                        }
                    }

                    is Outcome.Failure -> {

                        onLocationFailure(outcome)
                    }
                }
            }
        }
    }

    fun onLocationQueryResult(granted: Boolean, shouldShowRationale: Boolean) {

        viewModelScope.launch {

            when {

                granted -> onLocationPermissionGranted()

                shouldShowRationale -> _status.value = Status.NO_PERMISSION

                else -> _requestLocationPermission.trigger()
            }
        }
    }

    fun onLocationPermissionResult(granted: Boolean, shouldShowRationale: Boolean) {

        if (granted) {

            onLocationPermissionGranted()
        } else {

            permissionStatus = if (shouldShowRationale) PermissionStatus.RATIONALE else PermissionStatus.DENIED
            _status.value = Status.NO_PERMISSION
        }
    }

    private fun onLocationPermissionGranted() {

        _queryLocationServices.trigger()
    }

    fun onLocationServicesResult(enabled: Boolean, exception: ResolvableApiException?) {

        if (enabled) {

            collectLocation()
        } else {

            if (exception != null) locationServicesAPIException = exception

            if (!locationServicesRequested && exception != null) {

                _requestLocationServices.event = exception
            } else {

                _status.value = Status.LOCATION_ERROR
            }
        }
    }

    private fun collectLocation() {

        viewModelScope.launch {

            _status.value = Status.LOADING
            _loadingStatus.value = LoadingStatus.LOADING_LOCATION
            val l = getSelectedLocation()
            _selectedLocation.value = l
        }
    }

    fun onGrantPermissionClicked() {

        if (permissionStatus == PermissionStatus.RATIONALE) {

            _requestLocationPermission.trigger()
        } else {

            navigate(object : NavigationCommand {

                override fun navigateWith(context: Context, navController: NavController) {

                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", context.packageName, null)
                    intent.data = uri
                    context.startActivity(intent)
                }
            })
        }
    }

    fun onChooseLocationClicked() {

        navigate(R.id.action_mainFragmentContainer_to_fragmentLocations)
    }

    fun onRequestLocationServicesClicked() {

        _requestLocationServices.event = locationServicesAPIException
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

    fun onRetry() {

        collectLocation()
    }

    private suspend fun fetchForecast(location: Location) {

        _status.value = Status.LOADING
        _loadingStatus.value = LoadingStatus.LOADING_FORECAST

        val forecast = getNowForecast.invoke(location)

        if (forecast is Outcome.Success) {

            _status.value = Status.LOADED
            _currentForecast.value = forecast
        } else if (forecast is Outcome.Failure) {

            onLocationFailure(forecast)
        }
    }

    private fun onLocationFailure(failure: Outcome.Failure) {

        when (failure) {

            is LocationFailure -> _status.value = Status.LOCATION_ERROR
            is NetworkFailure -> _status.value = Status.NETWORK_ERROR
        }
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

    enum class Status {

        LOADING, NETWORK_ERROR, LOCATION_ERROR, NO_PERMISSION, LOADED
    }

    enum class LoadingStatus {

        LOADING_LOCATION, LOADING_FORECAST
    }

    enum class PermissionStatus {

        RATIONALE, DENIED
    }
}