package uk.henrytwist.projectbarry.domain.models

class SavedLocation(val id: Int, name: String, coordinates: LocationCoordinates, val pinned: Boolean) : Location(name, coordinates)