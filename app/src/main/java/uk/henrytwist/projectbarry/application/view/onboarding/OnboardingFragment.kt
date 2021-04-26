package uk.henrytwist.projectbarry.application.view.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceDataStore
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.projectbarry.databinding.OnboardingFragmentBinding

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private lateinit var binding: OnboardingFragmentBinding
    private val viewModel by viewModels<OnboardingViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = OnboardingFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.observeNavigation(this)

        binding.onboardingPager.adapter = OnboardingPageAdapter(this)

        binding.onboardingPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {

                viewModel.onPageSelected(position)
            }
        })

        viewModel.page.observe(viewLifecycleOwner) {

            binding.onboardingPager.currentItem = it
        }
    }
}