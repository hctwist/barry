package com.twisthenry8gmail.projectbarry.application.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.twisthenry8gmail.projectbarry.domain.core.LocationSearchResult
import com.twisthenry8gmail.projectbarry.domain.core.SavedLocation
import com.twisthenry8gmail.projectbarry.domain.usecases.*
import com.twisthenry8gmail.projectbarry.application.viewmodel.navigator.NavigatorViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LocationsViewModel @Inject constructor(
    private val searchLocationUseCase: SearchLocationUseCase,
    private val savedLocationsUseCase: GetSavedLocationsUseCase,
    private val selectLocationUseCase: SelectLocationUseCase,
    private val togglePinLocationUseCase: TogglePinLocationUseCase,
    private val saveSearchLocationUseCase: SaveSearchLocationUseCase
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

        val savedLocations = savedLocationsUseCase().sortedBy {

            if (it.pinned) 0 else 1
        }

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

            saveSearchLocationUseCase(result)
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