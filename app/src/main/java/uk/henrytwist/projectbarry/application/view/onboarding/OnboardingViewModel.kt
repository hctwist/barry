package uk.henrytwist.projectbarry.application.view.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.henrytwist.androidbasics.livedata.immutable
import uk.henrytwist.androidbasics.navigation.NavigatorViewModel
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.domain.models.ScaledSpeed
import uk.henrytwist.projectbarry.domain.models.ScaledTemperature
import uk.henrytwist.projectbarry.domain.usecases.CompleteOnboarding
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(private val completeOnboarding: CompleteOnboarding) : NavigatorViewModel() {

    private val nPages = OnboardingPage.values().size + 1

    private val _page = MutableLiveData(0)
    val page = _page.immutable()

    val showFinishButton = page.map { it == nPages - 1 }

    private var temperatureScale: ScaledTemperature.Scale? = null
    private var speedScale: ScaledSpeed.Scale? = null

    fun onNext() {

        val newPage = page.value!! + 1

        if (newPage == nPages) {

            onFinish()
        } else {

            onPageSelected(newPage)
        }
    }

    fun onPageSelected(position: Int) {

        _page.value = position
    }

    fun onSkip() {

        onFinish()
    }

    fun onTemperatureScaleChanged(scale: ScaledTemperature.Scale) {

        temperatureScale = scale
    }

    fun onSpeedScaleChanged(scale: ScaledSpeed.Scale) {

        speedScale = scale
    }

    private fun onFinish() {

        viewModelScope.launch {

            completeOnboarding(temperatureScale, speedScale)
            navigate(R.id.action_onboardingFragment_to_mainFragmentContainer)
        }
    }
}