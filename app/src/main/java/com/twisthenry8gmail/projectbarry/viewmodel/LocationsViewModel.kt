package com.twisthenry8gmail.projectbarry.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.twisthenry8gmail.projectbarry.data.Result
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocation
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocationRepository
import com.twisthenry8gmail.projectbarry.data.locations.LocationSearchResult
import com.twisthenry8gmail.projectbarry.view.locations.LocationChoiceAdapter
import com.twisthenry8gmail.projectbarry.viewmodel.navigator.NavigationCommand
import com.twisthenry8gmail.projectbarry.viewmodel.navigator.NavigatorViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationsViewModel @Inject constructor(
    private val forecastLocationRepository: ForecastLocationRepository
) : NavigatorViewModel() {

    private val _searching = MutableLiveData(false)
    val searching = _searching.distinctUntilChanged()

    private val _locationChoices = MutableLiveData<List<LocationChoiceAdapter.Choice>>()
    val locationChoices: LiveData<List<LocationChoiceAdapter.Choice>>
        get() = _locationChoices

    private val _searchResults = MutableLiveData<List<LocationSearchResult>>()
    val searchResults: LiveData<List<LocationSearchResult>>
        get() = _searchResults

    init {

        viewModelScope.launch {

            refreshChoices()
        }
    }

    suspend fun refreshChoices() {

        val savedLocations = forecastLocationRepository.getPinnedAndChosenPlaces().sortedBy {

            if (it.type == ForecastLocation.Type.CHOSEN) 0 else 1
        }.map {

            LocationChoiceAdapter.Choice.Location(it.placeId, it.type, it.name)
        }

        val currentLocation = LocationChoiceAdapter.Choice.CurrentLocation

        val choices = ArrayList<LocationChoiceAdapter.Choice>(savedLocations.size + 1)
        choices.add(currentLocation)
        choices.addAll(savedLocations)

        _locationChoices.value = choices
    }

    fun onClickChoice(choice: LocationChoiceAdapter.Choice) {

        if (choice is LocationChoiceAdapter.Choice.Location) {

            if (choice.type != ForecastLocation.Type.CHOSEN) {

                viewModelScope.launch {

                    forecastLocationRepository.removeAllOfType(ForecastLocation.Type.CHOSEN)
                }
            }
            forecastLocationRepository.select(choice.placeId)
        } else {

            forecastLocationRepository.removeSelection()
        }

        navigateBack()
    }

    fun onPin(choice: LocationChoiceAdapter.Choice) {

        if (choice is LocationChoiceAdapter.Choice.Location) {
            viewModelScope.launch {

                if (choice.type == ForecastLocation.Type.PINNED) {

                    val selected = forecastLocationRepository.getSelectedPlaceId()
                    if (choice.placeId == selected) {

                        forecastLocationRepository.removeSelection()
                    }
                    forecastLocationRepository.removeLocation(choice.placeId)

                    val newChoices = locationChoices.value?.minus(choice)
                    _locationChoices.value = newChoices
                } else {

                    forecastLocationRepository.pin(choice.placeId)
                    refreshChoices()
                }
            }
        }
    }

    fun onClickSearch(result: LocationSearchResult) {

        viewModelScope.launch {

            val location = forecastLocationRepository.getLocation(result.placeId)
            forecastLocationRepository.onAutocompleteSessionFinished()

            if (location is Result.Success) {

                location.data.let {

                    forecastLocationRepository.replaceAllOfType(it)
                    forecastLocationRepository.select(it.placeId)
                }

                navigate(NavigationCommand.Back)
            }
        }
    }

    fun onSearchTextChanged(text: String) {

        val shouldSearch = text.isNotEmpty()

        _searching.value = shouldSearch

        if (shouldSearch) {

            viewModelScope.launch {

                _searchResults.value = forecastLocationRepository.findPlaces(text)
            }
        } else {

            _searchResults.value = listOf()
        }
    }

    override fun onCleared() {

        forecastLocationRepository.onAutocompleteSessionFinished()
    }
}