package com.twisthenry8gmail.projectbarry.usecases

import com.twisthenry8gmail.projectbarry.data.locations.SelectedLocationLocalSourceImpl
import javax.inject.Inject

class NeedsLocationPermissionUseCase @Inject constructor(private val selectedLocationLocalSource: SelectedLocationLocalSourceImpl) {

    operator fun invoke(): Boolean {

        return selectedLocationLocalSource.getSelectedLocationId() == null
    }
}