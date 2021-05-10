package uk.henrytwist.projectbarry.domain.models

class SelectedLocation(val location: Location?, val type: Type) {

    enum class Type {

        CURRENT_WAITING, CURRENT, CURRENT_UNAVAILABLE, STATIC
    }
}