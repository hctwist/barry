package com.twisthenry8gmail.projectbarry.data.locations

class ForecastLocation(
    val placeId: String,
    val name: String,
    val lat: Double,
    val lng: Double,
    val type: Type
) {

    fun isAt(lat: Double, lng: Double) = this.lat == lat && this.lng == lng

    enum class Type {

        CURRENT_LOCATION, PINNED, CHOSEN
    }
}