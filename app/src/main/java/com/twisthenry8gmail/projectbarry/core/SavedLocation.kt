package com.twisthenry8gmail.projectbarry.core

class SavedLocation(val id: Int, name: String, lat: Double, lng: Double, val pinned: Boolean) :
    LocationData(name, lat, lng)