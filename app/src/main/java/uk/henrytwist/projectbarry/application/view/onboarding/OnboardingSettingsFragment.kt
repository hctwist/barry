package uk.henrytwist.projectbarry.application.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.databinding.OnboardingSettingsFragmentBinding
import uk.henrytwist.projectbarry.domain.models.ScaledSpeed
import uk.henrytwist.projectbarry.domain.models.ScaledTemperature

class OnboardingSettingsFragment : Fragment() {

    private val viewModel by viewModels<OnboardingViewModel>(ownerProducer = { requireParentFragment() })

    private var _binding: OnboardingSettingsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = OnboardingSettingsFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.onboardingSettingsTemperature.setOnClickListener {

            MaterialAlertDialogBuilder(requireContext())
                    .setItems(R.array.settings_temperature_scale_entries) { _, which ->

                        viewModel.onTemperatureScaleChanged(ScaledTemperature.Scale.values()[which])
                    }
                    .show()
        }

        binding.onboardingSettingsSpeed.setOnClickListener {

            MaterialAlertDialogBuilder(requireContext())
                    .setItems(R.array.settings_speed_scale_entries) { _, which ->

                        viewModel.onSpeedScaleChanged(ScaledSpeed.Scale.values()[which])
                    }
                    .show()
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()
        _binding = null
    }
}