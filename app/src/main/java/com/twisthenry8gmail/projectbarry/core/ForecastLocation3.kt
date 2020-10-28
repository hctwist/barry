package com.twisthenry8gmail.projectbarry.core

class ForecastLocation3(val name: String, val lat: Double, val lng: Double, val type: Type) {

    enum class Type {

        STATIC, PINNED, LAST_CURRENT, CURRENT
    }
}