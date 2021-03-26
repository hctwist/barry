package uk.henrytwist.projectbarry.application.view.locationmenu

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.henrytwist.androidbasics.navigation.NavigatorViewModel
import uk.henrytwist.projectbarry.domain.models.MenuLocation
import uk.henrytwist.projectbarry.domain.usecases.GetMenuLocations
import uk.henrytwist.projectbarry.domain.usecases.SelectLocationUseCase
import javax.inject.Inject

@HiltViewModel
class LocationMenuViewModel @Inject constructor(
        private val getMenuLocations: GetMenuLocations,
        private val selectLocationUseCase: SelectLocationUseCase
) : NavigatorViewModel() {

    val locations = liveData {

        emit(getMenuLocations.getMenuLocations())
    }

    fun onLocationClicked(location: MenuLocation) {

        viewModelScope.launch {

            selectLocationUseCase(location)
            navigateBack()
        }
    }
}