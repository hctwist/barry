package uk.henrytwist.projectbarry.application.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import uk.henrytwist.projectbarry.databinding.OnboardingPageFragmentBinding

class OnboardingPageFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val binding = OnboardingPageFragmentBinding.inflate(inflater, container, false)
        binding.page = OnboardingPage.values()[requireArguments().getInt(PAGE_KEY)]

        return binding.root
    }

    companion object {

        private const val PAGE_KEY = "page"

        fun getInstance(page: Int): Fragment {

            return OnboardingPageFragment().apply {

                val bundle = bundleOf(PAGE_KEY to page)
                arguments = bundle
            }
        }
    }
}