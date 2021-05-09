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

    private val _temperatureScale = MutableLiveData(ScaledTemperature.Scale.CELSIUS)
    val temperatureScale = _temperatureScale.immutable()

    private val _speedScale = MutableLiveData(ScaledSpeed.Scale.METRES_PER_SECOND)
    val speedScale = _speedScale.immutable()

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

        _temperatureScale.value = scale
    }

    fun onSpeedScaleChanged(scale: ScaledSpeed.Scale) {

        _speedScale.value = scale
    }

    private fun onFinish() {

        viewModelScope.launch {

            completeOnboarding(temperatureScale.value!!, speedScale.value!!)
            navigate(R.id.action_onboardingFragment_to_mainFragmentContainer)
        }
    }
}