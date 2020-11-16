package com.twisthenry8gmail.projectbarry.domain.uicore

sealed class MenuLocation(val selected: Boolean, val icon: LocationIcon) {

    class Current(selected: Boolean, icon: LocationIcon, val subtitle: String) :
        MenuLocation(selected, icon)

    class Saved(
        selected: Boolean,
        icon: LocationIcon,
        val id: Int,
        val title: String,
        val subtitle: String
    ) : MenuLocation(selected, icon)
}