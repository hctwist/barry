package uk.henrytwist.projectbarry.application.data.selectedlocation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.henrytwist.projectbarry.application.di.DataStoreProviderModule
import uk.henrytwist.projectbarry.domain.data.selectedlocation.SelectedLocationLocalSource
import javax.inject.Inject

class SelectedLocationLocalSourceImpl @Inject constructor(@DataStoreProviderModule.Data private val dataStore: DataStore<Preferences>) :
        SelectedLocationLocalSource {

    private val selectedLocationKey = intPreferencesKey("selected_location")

    override fun getSelectedLocationId(): Flow<Int?> {

        return dataStore.data.map { it[selectedLocationKey] }
    }

    override suspend fun select(id: Int) {

        dataStore.edit { it[selectedLocationKey] = id }
    }

    override suspend fun removeSelection() {

        dataStore.edit { it.remove(selectedLocationKey) }
    }
}