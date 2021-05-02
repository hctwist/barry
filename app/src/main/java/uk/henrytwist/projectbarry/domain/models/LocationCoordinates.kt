package uk.henrytwist.projectbarry.domain.models

import kotlin.math.*

data class LocationCoordinates(
        val lat: Double,
        val lng: Double
) {

    /**
     * Computes the distance between two location coordinates in metres.
     */
    fun distanceTo(otherCoordinates: LocationCoordinates): Double {

        val r = 6371000.0

        return 2 * r * asin(
                sqrt(sin(Math.toRadians(otherCoordinates.lat - lat) / 2).pow(2)
                        + cos(Math.toRadians(otherCoordinates.lat)) * cos(Math.toRadians(lat)) * sin((Math.toRadians(otherCoordinates.lng - lng)) / 2).pow(2)
                ))
    }

    override fun toString(): String {

        return "[latitude = $lat, longitude = $lng]"
    }
}