package uk.henrytwist.projectbarry.domain.models

class SavedLocation(placeId: String, name: String, coordinates: LocationCoordinates, val pinned: Boolean) : Location(placeId, name, coordinates)