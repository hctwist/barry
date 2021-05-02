package uk.henrytwist.projectbarry.domain.data.selectedlocation

import kotlinx.coroutines.flow.Flow

interface SelectedLocationLocalSource {

    fun getSelectedLocationId(): Flow<Int?>

    suspend fun select(id: Int)

    suspend fun removeSelection()
}