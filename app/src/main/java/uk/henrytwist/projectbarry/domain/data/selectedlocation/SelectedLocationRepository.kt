package uk.henrytwist.projectbarry.domain.data.selectedlocation

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectedLocationRepository @Inject constructor(private val localSource: SelectedLocationLocalSource) {

    fun getSelectedLocationId(): Flow<Int?> = localSource.getSelectedLocationId()

    suspend fun select(id: Int) {

        localSource.select(id)
    }

    suspend fun selectCurrentLocation() {

        localSource.removeSelection()
    }
}