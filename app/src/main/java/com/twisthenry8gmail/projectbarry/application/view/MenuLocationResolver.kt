package com.twisthenry8gmail.projectbarry.application.view

import android.content.Context
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.domain.uicore.MenuLocation


object MenuLocationResolver {

    fun resolveTitle(context: Context, location: MenuLocation): String {

        return when (location) {

            is MenuLocation.Current -> context.getString(R.string.current_location)
            is MenuLocation.Saved -> location.title
        }
    }

    fun resolveSubtitle(context: Context, location: MenuLocation): String {

        val subtitleString = when (location) {

            is MenuLocation.Current -> location.subtitle
            is MenuLocation.Saved -> location.subtitle
        }

        return if (subtitleString.isEmpty()) "" else context.getString(
            R.string.format_parenthesis,
            subtitleString
        )
    }
}