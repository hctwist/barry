package com.twisthenry8gmail.projectbarry.core

class SelectedLocation(
    val status: Status,
    val locationData: LocationData?
) {

    enum class Status {

        INTERMEDIATE_CURRENT_LOCATION, CURRENT_LOCATION, OUTDATED_CURRENT_LOCATION, STATIC_LOCATION
    }
}