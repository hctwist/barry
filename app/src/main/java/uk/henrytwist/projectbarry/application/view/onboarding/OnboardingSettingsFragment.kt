package uk.henrytwist.projectbarry.application.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.databinding.OnboardingSettingsFragmentBinding
import uk.henrytwist.projectbarry.domain.models.ScaledSpeed
import uk.henrytwist.projectbarry.domain.models.ScaledTemperature

class OnboardingSettingsFragment : Fragment() {

    private lateinit var binding: OnboardingSettingsFragmentBinding

    private val viewModel by viewModels<OnboardingViewModel>(ownerProducer = { requireParentFragment() })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = OnboardingSettingsFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.onboardingSettingsTemperature.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_text_view,
                resources.getStringArray(R.array.settings_temperature_scale_entries)
        )

        binding.onboardingSettingsTemperature.setOnItemSelected {

            viewModel.onTemperatureScaleChanged(ScaledTemperature.Scale.values()[it])
        }

        binding.onboardingSettingsSpeed.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_text_view,
                resources.getStringArray(R.array.settings_speed_scale_entries)
        )

        binding.onboardingSettingsSpeed.setOnItemSelected {

            viewModel.onSpeedScaleChanged(ScaledSpeed.Scale.values()[it])
        }
    }

    private inline fun Spinner.setOnItemSelected(crossinline action: (Int) -> Unit) {

        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                action(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }
}