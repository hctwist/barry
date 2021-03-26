package uk.henrytwist.projectbarry.domain.models

sealed class MenuLocation(val selected: Boolean) {

    class Current(selected: Boolean) : MenuLocation(selected)

    class Saved(
            selected: Boolean,
            val placeId: String,
            val name: String
    ) : MenuLocation(selected)
}