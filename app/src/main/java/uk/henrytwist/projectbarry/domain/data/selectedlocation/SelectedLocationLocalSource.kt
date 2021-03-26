package uk.henrytwist.projectbarry.domain.data.selectedlocation

interface SelectedLocationLocalSource {

    fun getSelectedLocationId(): String?

    fun select(placeId: String)

    fun removeSelection()
}