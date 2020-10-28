package com.twisthenry8gmail.projectbarry.usecases

import com.twisthenry8gmail.projectbarry.data.locations2.SelectedLocationLocalSource
import javax.inject.Inject

class NeedsLocationPermissionUseCase @Inject constructor(private val selectedLocationLocalSource: SelectedLocationLocalSource) {

    operator fun invoke(): Boolean {

        return selectedLocationLocalSource.getSelectedLocationId() == null
    }
}