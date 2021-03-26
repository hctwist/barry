package uk.henrytwist.projectbarry.domain.models

open class Location(val placeId: String, val name: String, val coordinates: LocationCoordinates) {

    override fun equals(other: Any?): Boolean {

        val otherLocation = other as? Location ?: return false
        return placeId == otherLocation.placeId
    }

    override fun hashCode(): Int {

        return placeId.hashCode()
    }
}