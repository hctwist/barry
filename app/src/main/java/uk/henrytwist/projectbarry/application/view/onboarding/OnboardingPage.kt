package uk.henrytwist.projectbarry.application.view.onboarding

import uk.henrytwist.projectbarry.R

enum class OnboardingPage(val iconRes: Int, val titleRes: Int, val bodyRes: Int) {

    INTRO(R.drawable.icon_clear_day, R.string.onboarding_intro_title, R.string.onboarding_intro_body),
    FORECASTS(R.drawable.icon_rain, R.string.onboarding_forecasts_title, R.string.onboarding_forecasts_body)
}