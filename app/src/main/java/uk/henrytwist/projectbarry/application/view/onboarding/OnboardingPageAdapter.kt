package uk.henrytwist.projectbarry.application.view.onboarding

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingPageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {

        return OnboardingPage.values().size + 1
    }

    override fun createFragment(position: Int): Fragment {

        return if (position == itemCount - 1) {

            OnboardingSettingsFragment()
        } else {

            OnboardingPageFragment.getInstance(position)
        }
    }
}