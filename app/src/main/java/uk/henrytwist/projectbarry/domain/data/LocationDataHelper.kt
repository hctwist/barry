package uk.henrytwist.projectbarry.domain.data

import uk.henrytwist.projectbarry.domain.models.LocationCoordinates
import kotlin.math.*

object LocationDataHelper {

    const val ACCEPTABLE_DISTANCE = 2000

    fun distanceBetween(coordinates: LocationCoordinates, otherCoordinates: LocationCoordinates): Double {

        val r = 6371000.0
        return 2 * r * asin(
                sqrt(sin((otherCoordinates.lat - coordinates.lat) / 2).pow(2)
                        + cos(otherCoordinates.lat) * cos(coordinates.lat) * sin((otherCoordinates.lng - coordinates.lng) / 2).pow(2)
                ))
    }
}