package uk.henrytwist.projectbarry.domain.usecases

import uk.henrytwist.kotlinbasics.sortedWithFieldComparator
import uk.henrytwist.projectbarry.domain.data.savedlocations.SavedLocationsRepository
import uk.henrytwist.projectbarry.domain.models.SavedLocation
import javax.inject.Inject

class GetSavedLocations @Inject constructor(private val savedLocationsRepository: SavedLocationsRepository) {

    suspend operator fun invoke(): List<SavedLocation> {

        return savedLocationsRepository.getLocations().sortedWithFieldComparator {

            addGroup {

                predicate { it.pinned }
                addField { it.name }
            }

            addField { it.name }
        }
    }
}