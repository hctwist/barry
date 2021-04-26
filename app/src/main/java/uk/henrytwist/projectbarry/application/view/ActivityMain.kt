package uk.henrytwist.projectbarry.application.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.data.settings.SettingsRepositoryImpl
import uk.henrytwist.projectbarry.domain.data.experience.ExperienceRepository
import javax.inject.Inject

@AndroidEntryPoint
class ActivityMain : AppCompatActivity(R.layout.activity_main) {

    @Inject
    lateinit var experienceRepository: ExperienceRepository

    override fun onCreate(savedInstanceState: Bundle?) {

        AppCompatDelegate.setDefaultNightMode(
                SettingsRepositoryImpl.getDarkMode(resources, PreferenceManager.getDefaultSharedPreferences(this))
        )

        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        val navController = navHostFragment.navController
        val graph = navController.navInflater.inflate(R.navigation.nav_main)

        lifecycleScope.launchWhenCreated {

            graph.startDestination = if (!experienceRepository.hasCompletedOnboarding()) {

                R.id.onboardingFragment
            } else {

                R.id.mainFragmentContainer
            }

            navController.graph = graph
        }
    }
}