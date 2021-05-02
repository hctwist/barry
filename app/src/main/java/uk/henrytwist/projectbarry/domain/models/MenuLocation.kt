package uk.henrytwist.projectbarry.domain.models

sealed class MenuLocation(val selected: Boolean) {

    class Current(selected: Boolean) : MenuLocation(selected)

    class Saved(
            selected: Boolean,
            val id: Int,
            val name: String
    ) : MenuLocation(selected)
}