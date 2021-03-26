package uk.henrytwist.projectbarry.application.view.locations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import uk.henrytwist.androidbasics.navigation.NavigatorViewModel
import uk.henrytwist.projectbarry.domain.models.LocationSearchResult
import uk.henrytwist.projectbarry.domain.models.SavedLocation
import uk.henrytwist.projectbarry.domain.usecases.*
import javax.inject.Inject

class LocationsViewModel @Inject constructor(
        private val searchLocationUseCase: SearchLocationUseCase,
        private val savedLocationsUseCase: GetSavedLocations,
        private val selectLocationUseCase: SelectLocationUseCase,
        private val togglePinLocationUseCase: TogglePinLocationUseCase,
        private val saveSearchLocation: SaveSearchLocation
) : NavigatorViewModel() {

    private val _searching = MutableLiveData(false)
    val searching = _searching.distinctUntilChanged()

    private val _savedLocations = MutableLiveData<List<SavedLocation>>()
    val savedLocations: LiveData<List<SavedLocation>>
        get() = _savedLocations

    private val _searchResults = MutableLiveData<List<LocationSearchResult>>()
    val searchResults: LiveData<List<LocationSearchResult>>
        get() = _searchResults

    init {

        viewModelScope.launch {

            refreshChoices()
        }
    }

    private suspend fun refreshChoices() {

        val savedLocations = savedLocationsUseCase()

        _savedLocations.value = savedLocations
    }

    fun onClickChoice(location: SavedLocation) {

        viewModelScope.launch {

            selectLocationUseCase(location)
            navigateBack()
        }
    }

    fun onPin(location: SavedLocation) {

        viewModelScope.launch {

            togglePinLocationUseCase(location)
            refreshChoices()
        }
    }

    fun onClickSearch(result: LocationSearchResult) {

        viewModelScope.launch {

            saveSearchLocation(result)
            navigateBack()
        }
    }

    fun onSearchTextChanged(text: String) {

        val shouldSearch = text.isNotEmpty()

        _searching.value = shouldSearch

        if (shouldSearch) {

            viewModelScope.launch {

                val results = searchLocationUseCase(text)
                results.ifSuccessful {

                    _searchResults.value = it
                }
            }
        } else {

            _searchResults.value = listOf()
        }
    }
}