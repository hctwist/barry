package com.twisthenry8gmail.projectbarry.domain.locations

interface SelectedLocationLocalSource {

    fun getSelectedLocationId(): Int?

    fun select(id: Int)

    fun removeSelection()
}