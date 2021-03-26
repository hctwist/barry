package uk.henrytwist.projectbarry.application.data

import uk.henrytwist.projectbarry.domain.data.PremiumRepository
import javax.inject.Inject

class PremiumRepositoryImpl @Inject constructor() : PremiumRepository {

    override fun hasPremium(): Boolean {

        return false
    }
}